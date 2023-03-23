package fr.miage.conference.api.controller;

import fr.miage.conference.api.assembler.SessionAssembler;
import fr.miage.conference.api.dto.SessionInput;
import fr.miage.conference.session.SessionService;
import fr.miage.conference.session.entity.Session;
import fr.miage.conference.session.exception.CannotAddToConferenceException;
import fr.miage.conference.session.exception.SessionNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/conferences/{conferenceId}/sessions")
public class SessionController {

    @EJB
    private SessionService service;

    @EJB
    private SessionAssembler assembler;

    private static ModelMapper modelMapper = new ModelMapper();

    @ApiOperation(value = "Create a session for a conference (ADMIN only.")
    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<Session>> createSession(@PathVariable String conferenceId, @RequestBody @Valid SessionInput session) throws CannotAddToConferenceException, SessionNotFoundException {
        Session saved = service.createSession(conferenceId, modelMapper.map(session, Session.class));
        URI location = linkTo(methodOn(SessionController.class).getSession(conferenceId, saved.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(saved));
    }

    @ApiOperation(value = "Get all sessions for a conference.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Session>>> getSessions(@PathVariable String conferenceId) {
        return ResponseEntity.ok(assembler.toCollectionModel(conferenceId, service.getSessionsFromConference(conferenceId)));
    }

    @ApiOperation(value = "Get a session for a conference.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Session>> getSession(@PathVariable String conferenceId, @PathVariable String id) throws SessionNotFoundException {
        return ResponseEntity.ok(assembler.toModel(service.getSession(conferenceId, id)));
    }
}
