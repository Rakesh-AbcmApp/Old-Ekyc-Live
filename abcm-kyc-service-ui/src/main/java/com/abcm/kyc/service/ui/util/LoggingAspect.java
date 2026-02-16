/*
 * package com.abcm.kyc.service.ui.util;
 * 
 * import java.util.stream.Collectors; import java.util.stream.IntStream;
 * 
 * import org.aspectj.lang.ProceedingJoinPoint; import
 * org.aspectj.lang.annotation.Around; import
 * org.aspectj.lang.annotation.Aspect; import
 * org.aspectj.lang.reflect.MethodSignature; import org.slf4j.Logger; import
 * org.slf4j.LoggerFactory; import org.springframework.stereotype.Component;
 * 
 * 
 * @Aspect
 * 
 * @Component public class LoggingAspect {
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(LoggingAspect.class);
 * 
 * @Around("execution(* com.abcm.kyc.service.ui.controller..*(..)) || execution(* com.abcm.kyc.service.ui.service..*(..))"
 * ) public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable
 * { MethodSignature signature = (MethodSignature) joinPoint.getSignature();
 * String className = signature.getDeclaringType().getSimpleName(); String
 * methodName = signature.getName(); String[] paramNames =
 * signature.getParameterNames(); Object[] args = joinPoint.getArgs();
 * logger.info("📥 {}.{} | Request: {}", className, methodName,
 * filterArgs(paramNames, args)); Object result = joinPoint.proceed(); // If
 * needed, uncomment for response logging logger.info("✅ {}.{} | Response: {}",
 * className, methodName, result); return result; } private String
 * filterArgs(String[] names, Object[] values) { return IntStream.range(0,
 * Math.min(names.length, values.length)) .filter(i -> values[i] != null)
 * .filter(i -> { String className = values[i].getClass().getName(); return !(
 * className.matches(
 * ".*(ServletRequest|ServletResponse|MultipartFile|Model|BindingResult|Principal).*")
 * || className.startsWith("org.springframework.web.context.request") ||
 * className.startsWith("org.springframework.security") ||
 * className.startsWith("org.apache.catalina") ); }) .mapToObj(i -> names[i] +
 * "=" + values[i]) .collect(Collectors.joining(", ", "[", "]")); }
 * 
 * 
 * }
 */