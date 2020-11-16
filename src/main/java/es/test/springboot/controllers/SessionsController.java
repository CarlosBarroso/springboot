package es.test.springboot.controllers;

import es.test.springboot.hateoas.SessionModelAssembler;
import es.test.springboot.entities.Session;
import es.test.springboot.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionModelAssembler sessionModelAssembler;
    
    @GetMapping
    public Page<EntityModel<Session>> list(@PageableDefault(size = 10) Pageable pageable){

        Page<Session> pageSessions = sessionRepository.findAll( pageable);

        List<EntityModel<Session>> listSessions =  pageSessions.getContent()
                .stream()
                .map(sessionModelAssembler::toModel)
                .collect(Collectors.toList());

        return new PageImpl<EntityModel<Session>>(listSessions,pageable, pageSessions.getTotalElements());

    }

    @GetMapping
    @RequestMapping("{id}")
    public EntityModel<Session> get(@PathVariable Long id){
        return sessionModelAssembler.toModel(
                sessionRepository.getOne(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session create (@RequestBody final Session sessionModel){
        return sessionRepository.saveAndFlush(sessionModel);
    }

    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    public void delete (@PathVariable Long id) {
        sessionRepository.deleteById(id);
    }

    @RequestMapping(value="{id}", method =RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session){
        Session existingSession = sessionRepository.getOne(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }


}
