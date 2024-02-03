package io.hrushik09.tasker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("io.hrushik09.tasker.aspect.PointcutDeclarations.forImportantClasses()")
    public void logMethodInvocationInfo(JoinPoint joinPoint) {
        logger.debug("invoking method: {} with", joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.debug("  argument: {}", arg);
        }
    }

    @AfterReturning(pointcut = "io.hrushik09.tasker.aspect.PointcutDeclarations.forImportantClasses()", returning = "result")
    public void logMethodResult(JoinPoint joinPoint, Object result) {
        logger.debug("returning from method: {} with", joinPoint.getSignature().toShortString());
        logger.debug("  result: {}", result);
    }

    @AfterThrowing(pointcut = "io.hrushik09.tasker.aspect.PointcutDeclarations.forImportantClasses()", throwing = "exception")
    public void logExceptionInfo(JoinPoint joinPoint, Throwable exception) {
        logger.error("error during method: {} with", joinPoint.getSignature().toShortString());
        logger.error("  message: {}", exception.getMessage());
    }
}
