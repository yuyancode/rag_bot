package org.wcw.user.service.impl;



import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.wcw.user.domain.convert.UserInfoConverter;
import org.wcw.user.mapper.UserInfoMapper;
import org.wcw.utils.EmailUtil;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl {
    private final UserInfoMapper userInfoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailUtil emailUtil;
    private final UserInfoConverter userInfoConverter;
    private final Cache<Object, Object> emailCache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build();


}
