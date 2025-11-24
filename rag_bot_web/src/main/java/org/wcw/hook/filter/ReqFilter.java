package org.wcw.hook.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.wcw.utils.SelfTraceIdGenerator;

import java.io.IOException;


public class ReqFilter implements Filter {
    private final String TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 设置请求链路唯一标识 traceId
            MDC.put(TRACE_ID, SelfTraceIdGenerator.generate());

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
