/*
package com.abcm.addhar_service.logging;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Apply logging only to controller and service packages
    @Around("execution(* com.abcm.addhar_service.controller..*(..)) || execution(* com.abcm.addhar_service.service..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        String input = filterArgs(paramNames, args);
        logger.info("📥 Aadhar api Request: {}.{} {}", className, methodName, input);
        try {
            Object result = joinPoint.proceed();
            logger.info("✅  Aadhar api Response: {}.{} {}", className, methodName, result);
            return result;
        } catch (Throwable ex) {
            //logger.error("❌ Exception in {}.{} | Error: {}", className, methodName, ex.getMessage());
            throw ex; // rethrow the exception to preserve flow
        }
    }

    private String filterArgs(String[] names, Object[] values) {
        return IntStream.range(0, Math.min(names.length, values.length))
            .filter(i -> values[i] != null)
            .filter(i -> {
                String className = values[i].getClass().getName();
                return !(
                    className.matches(".*(ServletRequest|ServletResponse|MultipartFile|Model|BindingResult|Principal).*") ||
                    className.startsWith("org.springframework.web.context.request") ||
                    className.startsWith("org.springframework.security") ||
                    className.startsWith("org.apache.catalina")
                );
            })
            .mapToObj(i -> names[i] + "=" + values[i])
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
*/