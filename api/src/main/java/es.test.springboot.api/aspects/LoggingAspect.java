package es.test.springboot.api.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Aspect
@Configuration
public class LoggingAspect {


    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("@annotation(es.test.springboot.annotations.Log)")
    public Object log (ProceedingJoinPoint thisJoinPoint)
            throws Throwable {

        String methodName = thisJoinPoint.getSignature().getName();
        Object[] methodArgs = thisJoinPoint.getArgs();
        StringBuffer sb = new StringBuffer();
        sb.append("Call method ").append(methodName);

        if (methodArgs.length>0){
            sb.append(" with args ").append(methodArgs[0]);
        }
        else {
            sb.append(" with NO args ");
        }
        logger.info(sb.toString());
        Object result = thisJoinPoint.proceed();
        logger.info("Method " + methodName + " returns " + result);
        return result;
    }

}
