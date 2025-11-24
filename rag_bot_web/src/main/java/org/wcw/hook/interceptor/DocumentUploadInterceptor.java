package org.wcw.hook.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wcw.utils.ThreadLocalUtils;

@Component
public class DocumentUploadInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURL().toString();
        String memoryId = uri.substring(uri.lastIndexOf("/") + 1);
        String knowledgeLibId = request.getParameter("knowledgeLibId");

        if(StringUtils.hasText(memoryId))
            ThreadLocalUtils.set("memoryId", memoryId);
        if(StringUtils.hasText(knowledgeLibId))
            ThreadLocalUtils.set("knowledgeLibId", knowledgeLibId);

        return true;
    }

}
