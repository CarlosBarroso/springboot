package es.test.springboot.hateoas;

import es.test.springboot.controllers.SessionsController;
import es.test.springboot.entities.Session;

import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionModelAssembler implements RepresentationModelAssembler<Session, EntityModel<Session>> {


    @Override
    public EntityModel<Session> toModel(Session session) {

        return EntityModel.of(session,
                linkTo(methodOn(SessionsController.class).get(session.getSession_id())).withSelfRel(),
                linkTo(methodOn(SessionsController.class).list( PageRequest.of(0,10))).withRel("sessions"));

    }
}
