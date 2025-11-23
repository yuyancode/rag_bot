package org.wcw.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.StringUtil;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.util.StringUtils;
import org.wcw.common.constant.Constants;
import org.wcw.common.dto.TokenPayloadDTO;
import org.wcw.common.enums.ErrorEnum;
import org.wcw.common.exception.UnAuthorizedException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtils {

    /**
     * 生成access_token
     * @param payload
     * @return
     */
    public static String generateAccessToken(TokenPayloadDTO payload) {
        if (payload ==  null) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", payload.getUserId());
        claims.put("username", payload.getUsername());
        // avatarUrl可能为null, 使用安全的方式添加
        if (payload.getAvatarUrl() != null) {
            claims.put("avatarUrl", payload.getAvatarUrl());
        } else {
            claims.put("avatarUrl", "");
        }

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setIssuer("wcw")
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, Constants.KEY)
                .compact();
    }

    /**
     * 获取access_token中的payload
     * @param token
     * @return
     */

    public static TokenPayloadDTO parseToken(String token) {
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(Constants.KEY)
                        .parseClaimsJws(token)
                        .getBody();
                return TokenPayloadDTO.builder()
                        .userId(claims.get("userId", Long.class))
                        .username(claims.get("username", String.class))
                        .avatarUrl(claims.get("avatarUrl", String.class))
                        .build();
            } catch (Exception e) {
                log.info("解析token失败", e);
                throw new UnAuthorizedException(ErrorEnum.ACCESS_TOKEN_INVALID);
            }
        }
        return null;
    }
}
