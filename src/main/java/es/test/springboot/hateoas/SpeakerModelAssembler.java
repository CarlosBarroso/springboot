package es.test.springboot.hateoas;

import es.test.springboot.controllers.SpeakerController;
import es.test.springboot.models.Speaker;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SpeakerModelAssembler implements RepresentationModelAssembler<Speaker, EntityModel<Speaker>> {

    @Override
    public EntityModel<Speaker> toModel(Speaker speaker) {

        return EntityModel.of(speaker,
                linkTo(methodOn(SpeakerController.class).get(speaker.getSpeaker_id())).withSelfRel(),
                linkTo(methodOn(SpeakerController.class).list( PageRequest.of(0,10))).withRel("speakers"));

    }

}
