package fr.miage.conference.api.controller;

import fr.miage.conference.conference.ConferenceService;
import fr.miage.conference.api.assembler.ConferenceAssembler;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.api.dto.ConferenceInput;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/conferences", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConferenceController {

    @EJB
    private ConferenceService service;

    @EJB
    private ConferenceAssembler assembler;

    private static ModelMapper modelMapper = new ModelMapper();

    @GetMapping()
    public List<Conference> getAllConferences() {
        return service.getAllConferences();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Conference>> getConference(@PathVariable("id") String id) throws ConferenceNotFoundException {
        return Optional.of(service.getConference(id))
                .map(i -> ResponseEntity.ok(assembler.toModel(i)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EntityModel<Conference>> createConference(@RequestBody @Valid ConferenceInput conference) {
        Conference saved = service.createConference(modelMapper.map(conference, Conference.class));
        URI location = linkTo(ConferenceController.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(saved));
    }

    @PatchMapping(value = "/{id}")
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EntityModel<Conference>> updateConference(@PathVariable("id") String id, @RequestBody ConferenceInput conference) throws ConferenceNotFoundException {
        Conference saved = service.updateConference(id, modelMapper.map(conference, Conference.class));
        URI location = linkTo(ConferenceController.class).slash(saved.getId()).toUri();
        return ResponseEntity.ok(assembler.toModel(saved));
    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Conference> deleteConference(@PathVariable("id") String id) throws ConferenceNotFoundException {
        service.deleteConference(id);
        return ResponseEntity.noContent().build();
    }
}