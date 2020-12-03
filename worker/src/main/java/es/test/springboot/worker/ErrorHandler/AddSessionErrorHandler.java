package es.test.springboot.worker.ErrorHandler;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class AddSessionErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t){
        t.printStackTrace();
        throw new AmqpRejectAndDontRequeueException(t);
    }
}
