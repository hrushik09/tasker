package io.hrushik09.tasker.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutDeclarations {
    @Pointcut("execution(* io.hrushik09.tasker.*.*Controller.*(..))")
    public void forControllerClasses() {
    }

    @Pointcut("execution(* io.hrushik09.tasker.*.*Service.*(..))")
    public void forServiceClasses() {
    }

    @Pointcut("execution(* io.hrushik09.tasker.*.*Repository.*(..))")
    public void forRepositoryClasses() {
    }

    @Pointcut("forControllerClasses() || forServiceClasses() || forRepositoryClasses()")
    public void forImportantClasses() {
    }
}
