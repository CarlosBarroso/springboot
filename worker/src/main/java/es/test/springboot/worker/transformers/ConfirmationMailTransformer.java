package es.test.springboot.worker.transformers;


import es.test.springboot.worker.database.entities.Session;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationMailTransformer {
    public String toMailText(Session session) {
        return new StringBuilder()
                .append("Body del mail\n")
                .append(session.getSession_name())
                .toString();
    }
}
