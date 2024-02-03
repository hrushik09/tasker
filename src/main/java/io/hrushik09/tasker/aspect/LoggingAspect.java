package io.hrushik09.tasker.aspect;

import org.aspectj.lang.JoinPoint;
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
        logger.debug("calling method: {} with", joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.debug("  argument: {}", arg);
        }
    }
}
