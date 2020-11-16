package es.test.springboot.controllers;


import es.test.springboot.models.SpeakerModel;
import org.springframework.beans.factory.annotation.Autowired;

import es.test.springboot.hateoas.SpeakerModelAssembler;
import es.test.springboot.entities.Speaker;
import es.test.springboot.repositories.SpeakerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.BeanUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakerController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SpeakerModelAssembler speakerModelAssembler;

    @Autowired
    private PagedResourcesAssembler<Speaker> pagedResourcesAssembler;

    @GetMapping
    public  PagedModel<SpeakerModel>  list(@PageableDefault(size = 10) Pageable pageable){
        return pagedResourcesAssembler.toModel(
                speakerRepository.findAll( pageable),
                speakerModelAssembler);
    }

    @GetMapping
    @RequestMapping("{id}")
    public SpeakerModel get(@PathVariable Long id){
        return speakerModelAssembler.toModel(
                speakerRepository.getOne(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Speaker create (@RequestBody final Speaker speakerModel){
        return speakerRepository.saveAndFlush(speakerModel);
    }

    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    public void delete (@PathVariable Long id) {
        speakerRepository.deleteById(id);
    }

    @RequestMapping(value="{id}", method =RequestMethod.PUT)
    public Speaker update(@PathVariable Long id, @RequestBody Speaker speakerModel){
        Speaker existingSpeakerModel = speakerRepository.getOne(id);
        BeanUtils.copyProperties(speakerModel, existingSpeakerModel, "speaker_id");
        return speakerRepository.saveAndFlush(existingSpeakerModel);
    }

}
