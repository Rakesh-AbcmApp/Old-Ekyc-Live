/*
 * package com.abcm.gst_service.logging;
 * 
 * import lombok.extern.slf4j.Slf4j; // Lombok import import
 * org.aspectj.lang.ProceedingJoinPoint; import
 * org.aspectj.lang.annotation.Around; import
 * org.aspectj.lang.annotation.Aspect; import
 * org.aspectj.lang.reflect.MethodSignature; import
 * org.springframework.stereotype.Component;
 * 
 * import java.util.Map; import java.util.concurrent.ConcurrentHashMap;
 * 
 * @Aspect
 * 
 * @Component
 * 
 * @Slf4j public class LoggingAspect {
 * 
 * 
 * private final Map<MethodSignature, String[]> paramNamesCache = new
 * ConcurrentHashMap<>();
 * 
 * @Around("execution(* com.abcm.gst_service..*(..))") public Object
 * logMethodRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
 * MethodSignature signature = (MethodSignature) joinPoint.getSignature();
 * 
 * 
 * String[] paramNames = paramNamesCache.computeIfAbsent(signature,
 * MethodSignature::getParameterNames);
 * 
 * String className = signature.getDeclaringType().getSimpleName(); String
 * methodName = signature.getName();
 * 
 * if (log.isInfoEnabled()) { String request = buildRequestString(paramNames,
 * joinPoint.getArgs()); // log.info("📥 Request: {}.{} [{}]", className,
 * methodName, request); }
 * 
 * Object response = joinPoint.proceed();
 * 
 * if (log.isInfoEnabled()) { log.info("✅ Response: {}.{} [{}]", className,
 * methodName, response); }
 * 
 * return response; }
 * 
 * private String buildRequestString(String[] paramNames, Object[] args) { if
 * (paramNames == null || args == null) return "no params";
 * 
 * int len = Math.min(paramNames.length, args.length); if (len == 0) return
 * "no params";
 * 
 * return java.util.stream.IntStream.range(0, len) .mapToObj(i -> paramNames[i]
 * + "=" + (args[i] != null ? args[i] : "null"))
 * .collect(java.util.stream.Collectors.joining(", ")); } }
 */