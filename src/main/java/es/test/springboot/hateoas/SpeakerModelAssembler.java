package es.test.springboot.hateoas;

import es.test.springboot.controllers.SpeakerController;
import es.test.springboot.models.Speaker;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SpeakerModelAssembler
        extends RepresentationModelAssemblerSupport<Speaker, EntityModel<Speaker>> {

    public SpeakerModelAssembler(Class<?> controllerClass, Class<EntityModel<Speaker>> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public EntityModel<Speaker> toModel(Speaker speaker) {

        return EntityModel.of(speaker,
                linkTo(methodOn(SpeakerController.class).get(speaker.getSpeaker_id())).withSelfRel(),
                linkTo(methodOn(SpeakerController.class).list( PageRequest.of(0,10))).withRel("speakers"));

    }

    @Override
    public CollectionModel<EntityModel<Speaker>> toCollectionModel(Iterable<? extends Speaker> entities)
    {
        CollectionModel<EntityModel<Speaker>> actorModels = super.toCollectionModel(entities);

        actorModels.add(linkTo(methodOn(SpeakerController.class).list( PageRequest.of(0,10))).withSelfRel());

        return actorModels;
    }

}
