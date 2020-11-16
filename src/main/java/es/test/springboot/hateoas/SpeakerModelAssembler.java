package es.test.springboot.hateoas;

import es.test.springboot.models.Speaker;
import es.test.springboot.repositories.SpeakerRepository;
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
                linkTo(methodOn(SpeakerRepository.class).getOne(speaker.getSpeaker_id())).withSelfRel());

    }

}
