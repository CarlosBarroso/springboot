package es.test.springboot.hateoas;


import es.test.springboot.controllers.SessionsController;
import es.test.springboot.controllers.SpeakerController;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HomeModelAssembler  implements RepresentationModelAssembler<Map, EntityModel<Map>> {

    public HomeModelAssembler() {
    }

    @Override
    public EntityModel<Map> toModel(Map map) {

        return EntityModel.of(map,
                linkTo(methodOn(SessionsController.class).list(  PageRequest.of(0,10))).withRel("sesions"),
                linkTo(methodOn(SpeakerController.class).list( PageRequest.of(0,10))).withRel("speakers"));

    }
}
