package org.wcw.user.service.impl;



import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.wcw.common.constant.Constants;
import org.wcw.common.dto.TokenPayloadDTO;
import org.wcw.common.enums.ErrorEnum;
import org.wcw.common.exception.BusinessException;
import org.wcw.user.domain.convert.UserInfoConverter;
import org.wcw.user.domain.entity.UserInfoDO;
import org.wcw.user.domain.vo.request.*;
import org.wcw.user.domain.vo.response.UserInfoResponse;
import org.wcw.user.mapper.UserInfoMapper;
import org.wcw.user.service.IUserInfoService;
import org.wcw.utils.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.wcw.common.constant.Constants.REQ_CONTEXT;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements IUserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailUtil emailUtil;
    private final UserInfoConverter userInfoConverter;
    private final Cache<Object, Object> emailCodeCache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build();


    public UserInfoResponse login(HttpServletRequest req, HttpServletResponse resp, LoginCommand request) {
        UserInfoDO user = userInfoMapper.selectByUsername(request.getUsername());

        // 账号或者密码错误
        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return null;
        }

        // 获取token
        getAccessTokenAndRefresh(resp, TokenPayloadDTO.builder()
                .username(user.getUsername())
                .userId(user.getUserId())
                .avatarUrl(user.getAvatarUrl())
                .build());

        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .nickName(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public String refresh(HttpServletRequest req, HttpServletResponse resp, String refreshToken) {
        Boolean flag = stringRedisTemplate.hasKey(refreshToken);
        if (Boolean.FALSE.equals(flag)) {
            throw new BusinessException(ErrorEnum.REFRESH_TOKEN_INVALID);
        }

        // 校验refreshToken是否有效
        String json = stringRedisTemplate.opsForValue().get(refreshToken);
        TokenPayloadDTO requestInfo = JSON.parseObject(json, TokenPayloadDTO.class);
        if (refreshToken == null) {
            throw new BusinessException(ErrorEnum.REFRESH_TOKEN_INVALID.getDesc());
        }

        // 删除旧的refreshToken
        stringRedisTemplate.delete(refreshToken);
        CookieUtils.deleteCookieByName(req, resp, Constants.REFRESH_TOKEN_COOKIE_NAME);
        return getAccessTokenAndRefresh(resp, requestInfo);
    }

    /**
     * 获取accessToken和refreshToken
     *
     * @param requestInfo
     * @return
     */
    private String getAccessTokenAndRefresh(HttpServletResponse resp, TokenPayloadDTO requestInfo) {
        if (requestInfo == null) {
            throw new BusinessException(ErrorEnum.REFRESH_TOKEN_INVALID);
        }

        // 生成access token和refresh token
        String accessToken = JwtUtils.generateAccessToken(requestInfo);
        String refreshToken = UUID.randomUUID().toString();

        // redis/cookie保存refreshToken
        stringRedisTemplate.opsForValue().set(refreshToken, JSON.toJSONString(requestInfo), Duration.ofMillis(Constants.REFRESH_TOKEN_REDIS_EXPIRE_TIME));
        CookieUtils.addCookie(resp, Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken, Constants.REFRESH_TOKEN_EXPIRE_TIME);

        // cookie保存accessToken
        CookieUtils.addCookie(resp, Constants.ACCESS_TOKEN_COOKIE_NAME, accessToken, Constants.ACCESS_TOKEN_EXPIRE_TIME);

        return accessToken;
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        // 从cookie中获取实际的refresh token key
        String refreshTokenKey = CookieUtils.findValueByName(Constants.REFRESH_TOKEN_COOKIE_NAME, req);
        if (StringUtils.hasText(refreshTokenKey)) {
            stringRedisTemplate.delete(refreshTokenKey);  // 删除实际的refresh token
        }
        CookieUtils.deleteCookieByName(req, resp, Constants.REFRESH_TOKEN_COOKIE_NAME);
        CookieUtils.deleteCookieByName(req, resp, Constants.ACCESS_TOKEN_COOKIE_NAME);
        ThreadLocalUtils.remove(REQ_CONTEXT);
    }

    @Override
    public Long registry(RegistryCommand registryCommand) {
        UserInfoDO userInfoDO = getUserByUsername(registryCommand.getUsername());
        if (userInfoDO != null) {
            throw new BusinessException(ErrorEnum.USER_EXIST);
        }
        userInfoDO = UserInfoDO.builder()
                .userId(IdUtil.getSnowflake().nextId())
                .username(registryCommand.getUsername())
                .password(BCrypt.hashpw(registryCommand.getPassword(), BCrypt.gensalt()))
                .nickname(registryCommand.getUsername())
                .build();
        userInfoMapper.insert(userInfoDO);
        return userInfoDO.getUserId();
    }

    @Override
    public UserInfoDO getUserByUsername(String username) {
        return userInfoMapper.selectByUsername(username);
    }

    @Override
    public UserInfoDO getUserById(Long id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public UserInfoDO getUserByEmail(String email) {
        return userInfoMapper.selectByEmail(email);
    }

    @Override
    public void updateUserInfo(ModifyUserInfoCommand modifyUserInfoCommand) {
       UserInfoDO userInfoDO = new UserInfoDO();
       userInfoDO.setUserId(modifyUserInfoCommand.getUserId());

       MultipartFile avatarFile = modifyUserInfoCommand.getAvatar();
        try {
            //上传图片到oss
            String originalFilename = avatarFile.getOriginalFilename();
            String type = FileUtils.getTypeByFileName(originalFilename);
            // - 统一为 {userId}.png
            String avatarFileName = modifyUserInfoCommand.getUserId() + "." + type;
            String avatarUrl = OssUtil.upload("avatar", avatarFileName, avatarFile.getInputStream());
            userInfoDO.setAvatarUrl(avatarUrl);
        } catch (IOException e) {
            throw new BusinessException(ErrorEnum.FILE_UPLOAD_FAILED);
        }

        if (StringUtils.hasText(modifyUserInfoCommand.getNewPassword())) {
            userInfoDO.setPassword(BCrypt.hashpw(modifyUserInfoCommand.getNewPassword(), BCrypt.gensalt()));
        }
        if (StringUtils.hasText(modifyUserInfoCommand.getNickname())) {
            userInfoDO.setNickname(modifyUserInfoCommand.getNickname());
        }
        if(StringUtils.hasText(modifyUserInfoCommand.getDescription())) {
            userInfoDO.setDescription(modifyUserInfoCommand.getDescription());
        }
        userInfoMapper.updateById(userInfoDO);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userInfoMapper.deleteById(id) > 0;
    }

    @Override
    public List<UserInfoDO> listUsers() {
        return userInfoMapper.selectList();
    }

    @Override
    public UserDetailInfoResp queryUserDetailInfo(QueryUserDetailInfoRequest request) {
        UserInfoDO userInfoDO = userInfoMapper.selectById(request.getUserId());

        if (userInfoDO == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        long joinDays = Duration.between(userInfoDO.getCreateTime(), LocalDateTime.now()).toDays();
        return UserDetailInfoResp.builder()
                .username(userInfoDO.getUsername())
                .nickname(userInfoDO.getNickname())
                .avatarUrl(userInfoDO.getAvatarUrl())
                .description(userInfoDO.getDescription())
                .joinDays(joinDays)
                .email(userInfoDO.getEmail())
                .build();
    }

    @Override
    public void bindEmail(BindEmailCommand bindEmailCommand) {
        String emailCode = (String) emailCodeCache.getIfPresent(bindEmailCommand.getUserId());
        if (emailCode == null || !emailCode.equals(bindEmailCommand.getCode())) {
            throw new BusinessException(ErrorEnum.EMAIL_CODE_INVALID);
        }

        // 绑定邮箱
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setUserId(bindEmailCommand.getUserId());
        userInfoDO.setEmail(bindEmailCommand.getNewEmail());
        userInfoMapper.updateById(userInfoDO);
    }

    @Override
    public void sendEmailCode(SendEmailCommand sendEmailCommand) {
        // 1.缓存验证码
        String code = new Random().nextInt(900000) + 100000 + "";
        emailCodeCache.put(sendEmailCommand.getUserId(), code);

        // 2.发送验证码邮件
        emailUtil.sendCodeVerifyEmail(sendEmailCommand.getTo(), code);
    }


}
