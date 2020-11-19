package es.test.springboot.controllers;

import es.test.springboot.hateoas.SessionModelAssembler;
import es.test.springboot.database.entities.Session;
import es.test.springboot.models.SessionModel;
import es.test.springboot.database.repositories.SessionRepository;
import es.test.springboot.services.SessionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionModelAssembler sessionModelAssembler;

    @Autowired
    private PagedResourcesAssembler<Session> pagedResourcesAssembler;

    @Autowired
    private SessionService sessionService;


    @Autowired
    @Qualifier("registrationRequest")
    private MessageChannel registrationRequestChannel;

/*
    @Autowired
    @Qualifier("messageChannelAddSession")
    private MessageChannel messageChannelAddSession;

    @Autowired
    @Qualifier("messageChannelUpdateSession")
    private MessageChannel messageChannelUpdateSession;
*/

    @GetMapping
    public PagedModel<SessionModel> list(@PageableDefault(size = 10) Pageable pageable){

//        Page<Session> pageSessions = sessionRepository.findAll( pageable);

//        List<EntityModel<Session>> listSessions =  pageSessions
//                .getContent()
//                .stream()
//                .map(sessionModelAssembler::toModel)
//                .collect(Collectors.toList());

//        return new PageImpl<EntityModel<Session>>(listSessions,pageable, pageSessions.getTotalElements());
        return pagedResourcesAssembler.toModel(sessionRepository.findAll(pageable), sessionModelAssembler);
    }

    @GetMapping
    @RequestMapping("{id}")
    public SessionModel get(@PathVariable Long id){
        return sessionModelAssembler.toModel(
                sessionRepository.getOne(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create (@RequestBody final Session session)
    {
        Message<Session> message = MessageBuilder
                .withPayload(session)
                .setHeader("dateTime", OffsetDateTime.now())
                .build();

        registrationRequestChannel.send(message);
        //return ResponseEntity.created(new URI("tbc")).build();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> delete (@PathVariable Long id)
    {
        sessionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="{id}", method =RequestMethod.PUT)
    public ResponseEntity update(@PathVariable Long id, @RequestBody Session session)
    {
        Session existingSession = sessionRepository.getOne(id);

        BeanUtils.copyProperties(session, existingSession, "session_id");

        Message<Session> message = MessageBuilder
                .withPayload(existingSession)
                .build();

        //messageChannelUpdateSession.send(message);

        //return sessionService.update(existingSession);
        return ResponseEntity.noContent().build();
    }



}
