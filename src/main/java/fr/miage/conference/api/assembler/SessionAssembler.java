package fr.miage.conference.api.assembler;

import fr.miage.conference.api.controller.ConferenceController;
import fr.miage.conference.api.controller.ReservationController;
import fr.miage.conference.api.controller.SessionController;
import fr.miage.conference.session.entity.Session;
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
public class SessionAssembler implements RepresentationModelAssembler<Session, EntityModel<Session>> {

    @SneakyThrows
    @Override
    public EntityModel<Session> toModel(Session entity) {
        return EntityModel.of(entity,
            linkTo(methodOn(SessionController.class).getSession(entity.getConferenceId(), entity.getId())).withSelfRel(),
            linkTo(methodOn(ReservationController.class).createReservation(entity.getConferenceId(), entity.getId(), null, null)).withRel("reservation").withTitle("Do a reservation"),
            linkTo(methodOn(SessionController.class).getSessions(entity.getConferenceId())).withRel("sessions").withTitle("Others sessions of the conference")
        );
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
