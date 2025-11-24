package org.wcw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wcw.hook.filter.ReqFilter;
import org.wcw.hook.interceptor.DocumentUploadInterceptor;
import org.wcw.hook.interceptor.GlobalLoginInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final GlobalLoginInterceptor globalLoginInterceptor;

    private final DocumentUploadInterceptor documentUploadInterceptor;


    private final List<String> uploadDocExcludeUrls = List.of(
            "/chat/upload",
            "/chat/messages",
            "/chat/conversation-history",
            "/chat/conversation-create",
            "/chat/conversation-delete",
            "/chat/conversation-title-update"
    );

    private final List<String> loginExcludeUrls = List.of(
            "/user/login",
            "/user/registry",
            "/user/logout",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(loginExcludeUrls);
        registry.addInterceptor(documentUploadInterceptor)
                .addPathPatterns("/chat/**")
                .addPathPatterns("/library/createKnowledgeLibDocument")
                .addPathPatterns(uploadDocExcludeUrls);
    }

    /**
     * 请求链路追踪过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<ReqFilter> reqFilter() {
        FilterRegistrationBean<ReqFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new ReqFilter());
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }

}
