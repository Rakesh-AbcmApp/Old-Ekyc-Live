/*
 * package com.abcm.voterId.util;
 * 
 * 
 * import org.aspectj.lang.ProceedingJoinPoint; import
 * org.aspectj.lang.annotation.Around; import
 * org.aspectj.lang.annotation.Aspect; import
 * org.aspectj.lang.reflect.MethodSignature; import org.slf4j.Logger; import
 * org.slf4j.LoggerFactory; import org.springframework.stereotype.Component;
 * 
 * @Aspect
 * 
 * @Component public class LoggingAspect {
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(LoggingAspect.class);
 * 
 * // Apply logging to controller and service packages only
 * 
 * @Around("execution(* com.abcm.voterId_service.controller..*(..)) || execution(* com.abcm.voterId_service..*(..))"
 * ) public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws
 * Throwable { MethodSignature signature = (MethodSignature)
 * joinPoint.getSignature(); String className =
 * signature.getDeclaringType().getSimpleName(); String methodName =
 * signature.getName(); String[] paramNames = signature.getParameterNames();
 * Object[] args = joinPoint.getArgs(); String input = formatArgs(paramNames,
 * args); logger.info("📥 Request: {}.{} {}", className, methodName, input); try
 * { Object result = joinPoint.proceed(); logger.info("✅ Response: {}.{} {}",
 * className, methodName, result); return result; } catch (Throwable ex) {
 * logger.error("❌ Exception in {}.{} | Error: {}", className, methodName);
 * throw ex; } }
 * 
 * private String formatArgs(String[] names, Object[] values) { if (names ==
 * null || values == null) return "[]"; StringBuilder builder = new
 * StringBuilder("["); int len = Math.min(names.length, values.length); for (int
 * i = 0; i < len; i++) { builder.append(names[i]) .append("=")
 * .append(values[i] != null ? values[i].toString() : "null"); if (i < len - 1)
 * builder.append(", "); } builder.append("]"); return builder.toString(); } }
 */