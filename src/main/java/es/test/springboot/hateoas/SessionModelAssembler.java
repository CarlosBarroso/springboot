package es.test.springboot.hateoas;

import es.test.springboot.controllers.SessionsController;
import es.test.springboot.entities.Session;

import es.test.springboot.models.SessionModel;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionModelAssembler extends RepresentationModelAssemblerSupport<Session, SessionModel> {


    public SessionModelAssembler() {
        super(SessionsController.class, SessionModel.class);
    }


    @Override
    public SessionModel toModel(Session session) {

        SessionModel sessionModel = instantiateModel(session);

        BeanUtils.copyProperties(session, sessionModel);

        sessionModel.add(
                linkTo(
                        methodOn(SessionsController.class).get(session.getSession_id())
                ).withSelfRel()
        );

        sessionModel.add(
                linkTo(
                        methodOn(SessionsController.class).list( PageRequest.of(0,10))
                ).withRel("Sessions")
        );

        sessionModel.add(
                linkTo(
                        methodOn(SessionsController.class).delete( session.getSession_id())
                ).withRel("Delete")
        );

        sessionModel.add(
                linkTo(
                        methodOn(SessionsController.class).update( session.getSession_id(),new Session())
                ).withRel("Put")
        );

        sessionModel.add(
                linkTo(
                        methodOn(SessionsController.class).create( new Session())
                ).withRel("Post")
        );

        return sessionModel;
    }

    @Override
    public CollectionModel<SessionModel> toCollectionModel(Iterable<? extends Session> sessions)
    {
        CollectionModel<SessionModel> sessionModels = super.toCollectionModel(sessions);

        sessionModels.add(
                linkTo(
                        methodOn(SessionsController.class).list( PageRequest.of(0,10))
                ).withSelfRel()
        );

        return sessionModels;
    }

}
