package es.test.springboot.worker.aspects;


import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Aspect
@Configuration
public class MettricCounterAspect {

    @Autowired
    MeterRegistry meterRegistry;

    private Logger logger = Logger.getLogger(MettricCounterAspect.class.getName());

    @Around("@annotation(es.test.springboot.worker.annotations.MettricCounter)")
    public Object mettricCounter (ProceedingJoinPoint thisJoinPoint)
            throws Throwable {

        String className = thisJoinPoint.getClass().getName();
        String methodName = thisJoinPoint.getSignature().getName();

        StringBuilder sb = new StringBuilder()
                .append(className)
                .append(".")
                .append(methodName)
                .append(".counter");

        meterRegistry
                .counter(sb.toString())
                .increment();

        return thisJoinPoint.proceed();

        /*

        sb = new StringBuilder();
        sb.append(className)
                .append(".")
                .append(methodName)
                .append(".timer");
        return meterRegistry
                .timer(sb.toString())
                .record(() -> {

                    Object proceed = thisJoinPoint.proceed();
                    return proceed;

                } );
                */

    }
}
