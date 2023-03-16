package fr.miage.conference.api.assembler;

import fr.miage.conference.api.controller.ConferenceController;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.session.entity.Session;
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
public class SessionAssembler implements RepresentationModelAssembler<Session, EntityModel<Session>> {

    @Override
    public EntityModel<Session> toModel(Session entity) {
        return EntityModel.of(entity);
    }


    public CollectionModel<EntityModel<Session>> toCollectionModel(String conferenceId, Iterable<? extends Session> entities) {
        List<EntityModel<Session>> intervenantModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(intervenantModel,
                linkTo(ConferenceController.class).slash(conferenceId).slash("sessions").withRel("collection"));
    }
}
