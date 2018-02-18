package fr.upec.m2.projects.JavaEE.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TraceAspect {
    private static final Logger LOGGER = LogManager.getLogger(TraceAspect.class);

    @Pointcut("within(@fr.upec.m2.projects.JavaEE.annotation.Trace *)")
    public void beanAnnotatedWithTrace() {}

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Pointcut("execution(public *.new(..))")
    public void publicConstructor() {}

    @Pointcut("beanAnnotatedWithTrace() && publicMethod()")
    public void publicMethodInsideAClassMarkedWithTrace() {}

    @Pointcut("beanAnnotatedWithTrace() && publicConstructor()")
    public void publicConstructorInsideAClassMarkedWithTrace() {}

    @Before("publicConstructorInsideAClassMarkedWithTrace()")
    public void traceConstructor(JoinPoint joinPoint) {
        LOGGER.trace("Instantiating {}", joinPoint.getSignature().toShortString());
    }

    @Before("publicMethodInsideAClassMarkedWithTrace()")
    public void traceMethodeBefore(JoinPoint joinPoint) {
        LOGGER.trace("Entering {}", joinPoint.getSignature().toShortString());
    }

    @After("publicMethodInsideAClassMarkedWithTrace()")
    public void traceMethodeAfter(JoinPoint joinPoint) {
        LOGGER.trace("Leaving {}", joinPoint.getSignature().toShortString());
    }
}
