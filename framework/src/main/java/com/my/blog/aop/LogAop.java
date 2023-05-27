package com.my.blog.aop;

import com.alibaba.fastjson.JSON;
import com.my.blog.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Yang Fan
 * @since 2023/5/26 18:55
 */
@Component
@Aspect
@Slf4j
public class LogAop {
    @Pointcut("@annotation(com.my.blog.annotation.SystemLog)")
    public void pointCut() {
    }
    @Around("pointCut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
// 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return ret;
    }
    private void handleBefore(ProceedingJoinPoint joinPoint) {
//获取本次请求的request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            MethodSignature signature = (MethodSignature)
                    joinPoint.getSignature();
            String businessName =
                    signature.getMethod().getAnnotation(SystemLog.class).businessName();
            log.info("=============Start=============");
// 打印请求 URL
            log.info("URL : {}",
                    requestAttributes.getRequest().getRequestURL().toString());
// 打印描述信息
            log.info("BusinessName : {}", businessName);
// 打印 Http method
            log.info("HTTP Method : {}",
                    requestAttributes.getRequest().getMethod());
// 打印调用 controller 的全路径以及执行方法
            log.info("Class Method : {}.{}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
// 打印请求的 IP
            log.info("IP : {}",
                    requestAttributes.getRequest().getRemoteAddr());
// 打印请求入参
            log.info("Request Args : {}", joinPoint.getArgs());
        }
    }
    private void handleAfter(Object ret) {
// 打印出参
        log.info("Response Args : {}", JSON.toJSONString(ret));
    }
}
