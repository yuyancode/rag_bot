package org.wcw.hook.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wcw.common.constant.Constants;
import org.wcw.common.dto.TokenPayloadDTO;
import org.wcw.utils.CookieUtils;
import org.wcw.utils.JwtUtils;
import org.wcw.utils.ThreadLocalUtils;

/**
 * 全局登录拦截器
 */
@Component
@RequiredArgsConstructor
public class GlobalLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = CookieUtils.findValueByName(Constants.ACCESS_TOKEN_COOKIE_NAME, request);
        TokenPayloadDTO tokenPayloadDTO = JwtUtils.parseToken(token);
        if (tokenPayloadDTO == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 设置上下文信息
        ThreadLocalUtils.set(Constants.REQ_CONTEXT, tokenPayloadDTO);
        return true;
    }
}
