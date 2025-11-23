package org.wcw.user.service.impl;



import cn.hutool.crypto.digest.BCrypt;
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
import org.wcw.common.constant.Constants;
import org.wcw.common.dto.TokenPayloadDTO;
import org.wcw.common.enums.ErrorEnum;
import org.wcw.common.exception.BusinessException;
import org.wcw.user.domain.convert.UserInfoConverter;
import org.wcw.user.domain.entity.UserInfoDO;
import org.wcw.user.domain.vo.request.LoginCommand;
import org.wcw.user.domain.vo.response.UserInfoResponse;
import org.wcw.user.mapper.UserInfoMapper;
import org.wcw.user.service.IUserInfoService;
import org.wcw.utils.CookieUtils;
import org.wcw.utils.EmailUtil;
import org.wcw.utils.JwtUtils;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements IUserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailUtil emailUtil;
    private final UserInfoConverter userInfoConverter;
    private final Cache<Object, Object> emailCache = Caffeine.newBuilder()
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

}
