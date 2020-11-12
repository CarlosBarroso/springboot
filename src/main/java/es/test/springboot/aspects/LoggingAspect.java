package es.test.springboot.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.logging.Logger;

@Aspect
public class LoggingAspect {


    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("@annotation(es.test.springboot.annotations.Log")
    public Object log (ProceedingJoinPoint thisJoinPoint)
            throws Throwable {
        String methodName = thisJoinPoint.getSignature().getName();
        Object[] methodArgs = thisJoinPoint.getArgs();
        logger.info("Call method " + methodName + " with args " + methodArgs[0]);
        Object result = thisJoinPoint.proceed();
        logger.info("Method " + methodName + " returns " + result);
        return result;
    }

}
