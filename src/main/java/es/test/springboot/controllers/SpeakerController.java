package es.test.springboot.controllers;


import es.test.springboot.models.Speaker;
import es.test.springboot.repositories.SpeakerPagingRepository;
import es.test.springboot.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakerController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SpeakerPagingRepository speakerPagingRepository;


    @GetMapping
    public Page<Speaker> list(Pageable pageable){
        return speakerPagingRepository.findAll( pageable);
    }

    @GetMapping
    @RequestMapping("{id}")
    public Speaker get(@PathVariable Long id){
        return speakerRepository.getOne(id);
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
