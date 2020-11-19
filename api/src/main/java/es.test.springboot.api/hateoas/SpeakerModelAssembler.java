package es.test.springboot.api.hateoas;

import es.test.springboot.api.controllers.SpeakerController;
import es.test.springboot.api.database.entities.Speaker;
import es.test.springboot.api.models.SpeakerModel;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SpeakerModelAssembler
        extends RepresentationModelAssemblerSupport<Speaker, SpeakerModel> {


    public SpeakerModelAssembler() {
        super(SpeakerController.class, SpeakerModel.class);
    }

    @Override
    public SpeakerModel toModel(Speaker speaker) {

        SpeakerModel speakerModel = instantiateModel(speaker);

        BeanUtils.copyProperties(speaker, speakerModel);

        speakerModel.add(linkTo(methodOn(SpeakerController.class).get(speaker.getSpeaker_id())).withSelfRel());
        speakerModel.add(linkTo(methodOn(SpeakerController.class).list( PageRequest.of(0,10))).withRel("speakers"));

        return speakerModel;
    }

    @Override
    public CollectionModel<SpeakerModel> toCollectionModel(Iterable<? extends Speaker> entities)
    {
        CollectionModel<SpeakerModel> speakerModels = super.toCollectionModel(entities);

        speakerModels.add(linkTo(methodOn(SpeakerController.class).list( PageRequest.of(0,10))).withSelfRel());

        return speakerModels;
    }

}
