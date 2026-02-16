/*
 * package com.abcm.pan_service.logging;
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
 * @Around("execution(* com.abcm.pan_service..*(..))") public Object
 * logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
 * MethodSignature signature = (MethodSignature) joinPoint.getSignature();
 * String className = signature.getDeclaringType().getSimpleName(); String
 * methodName = signature.getName(); String[] paramNames =
 * signature.getParameterNames(); Object[] args = joinPoint.getArgs();
 * logger.info("📥 {}.{} | Request: {}", className, methodName,
 * formatArgs(paramNames, args)); Object result = joinPoint.proceed();
 * logger.info("✅ {}.{} | Response: {}", className, methodName, result); return
 * result; }
 * 
 * private String formatArgs(String[] names, Object[] values) { StringBuilder sb
 * = new StringBuilder("["); for (int i = 0; i < Math.min(names.length,
 * values.length); i++) { sb.append(names[i]).append("=").append(values[i]); if
 * (i < names.length - 1) sb.append(", "); } return sb.append("]").toString(); }
 * }
 */