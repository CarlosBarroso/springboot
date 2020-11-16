package es.test.springboot.controllers;


import es.test.springboot.hateoas.SpeakerModelAssembler;
import es.test.springboot.models.Speaker;
import es.test.springboot.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakerController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SpeakerModelAssembler speakerModelAssembler;

    @GetMapping
    public Page<Speaker> list(@PageableDefault(size = 10) Pageable pageable){
        return speakerRepository.findAll( pageable);
    }

    @GetMapping
    @RequestMapping("{id}")
    public EntityModel<Speaker> get(@PathVariable Long id){

        Speaker speaker = speakerRepository.getOne(id);
        return speakerModelAssembler.toModel(speaker);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Speaker create (@RequestBody final Speaker speaker){
        return speakerRepository.saveAndFlush(speaker);
    }


    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    public void delete (@PathVariable Long id) {
        speakerRepository.deleteById(id);
    }

    @RequestMapping(value="{id}", method =RequestMethod.PUT)
    public Speaker update(@PathVariable Long id, @RequestBody Speaker speaker){
        Speaker existingSpeaker= speakerRepository.getOne(id);
        BeanUtils.copyProperties(speaker, existingSpeaker, "speaker_id");
        return speakerRepository.saveAndFlush(existingSpeaker);
    }



}
