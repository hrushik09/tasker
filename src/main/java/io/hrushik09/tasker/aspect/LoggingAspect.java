package io.hrushik09.tasker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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
            logger.debug("\targument: {}", arg);
        }
    }

    @AfterReturning(pointcut = "io.hrushik09.tasker.aspect.PointcutDeclarations.forImportantClasses()", returning = "result")
    public void logMethodResult(JoinPoint joinPoint, Object result) {
        logger.debug("returning from method: {} with", joinPoint.getSignature().toShortString());
        logger.debug("\tresult: {}", result);
    }

    @AfterThrowing(pointcut = "io.hrushik09.tasker.aspect.PointcutDeclarations.forImportantClasses()", throwing = "exception")
    public void logExceptionInfo(JoinPoint joinPoint, Throwable exception) {
        logger.error("error during method: {} with", joinPoint.getSignature().toShortString());
        logger.error("\tmessage: {}", exception.getMessage());
    }

    @Around("io.hrushik09.tasker.aspect.PointcutDeclarations.forImportantClasses()")
    public Object logMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        logger.debug("execution time for method: {} is {} ms", proceedingJoinPoint.getSignature().toShortString(), end - start);
        return result;
    }
}
