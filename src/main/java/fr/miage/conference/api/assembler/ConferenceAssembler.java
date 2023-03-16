package fr.miage.conference.api.assembler;

import fr.miage.conference.api.controller.ConferenceController;
import fr.miage.conference.api.controller.SessionController;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.api.dto.ConferenceInput;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Singleton
@Startup
@Service
public class ConferenceAssembler implements RepresentationModelAssembler<Conference, EntityModel<Conference>> {
    @SneakyThrows
    @Override
    public EntityModel<Conference> toModel(Conference entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ConferenceController.class).getConference(entity.getId())).withSelfRel(),
                linkTo(methodOn(SessionController.class).getSessions(entity.getId())).withRel("sessions").withTitle("Sessions of the conference"),
                linkTo(methodOn(ConferenceController.class).findAllByRsql(null)).withRel("collection").withTitle("Search conferences")
        );
    }

    @Override
    public CollectionModel<EntityModel<Conference>> toCollectionModel(Iterable<? extends Conference> entities) {
        List<EntityModel<Conference>> intervenantModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(intervenantModel,
                linkTo(methodOn(ConferenceController.class)
                        .findAllByRsql(null)).withSelfRel());
    }
}
