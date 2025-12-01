package org.wcw.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.wcw.common.annotation.MdcDot;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("@annotation(mdcDot)")
    public Object log(ProceedingJoinPoint joinPoint, MdcDot mdcDot) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long endTime = System.currentTimeMillis();
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            log.info("执行方法{}.{},耗时{}ms", className, methodName, endTime - startTime);
        }
    }
}
