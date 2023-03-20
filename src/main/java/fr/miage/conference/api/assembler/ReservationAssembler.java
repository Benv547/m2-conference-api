package fr.miage.conference.api.assembler;

import fr.miage.conference.api.controller.ConferenceController;
import fr.miage.conference.api.controller.ReservationController;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessPaymentException;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;
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
public class ReservationAssembler implements RepresentationModelAssembler<Reservation, EntityModel<Reservation>> {
    @Override
    public EntityModel<Reservation> toModel(Reservation entity) {
        if (entity.isPayee() || entity.isAnnulee())
            return EntityModel.of(entity,
                    linkTo(methodOn(ReservationController.class).getReservation(entity.getConferenceId(), entity.getSessionId(), entity.getUserId())).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).getReservations(entity.getConferenceId(), entity.getSessionId())).withRel("collection").withTitle("Search reservations"));
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(ReservationController.class).getReservation(entity.getConferenceId(), entity.getSessionId(), entity.getUserId())).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).paymentReservation(entity.getConferenceId(), entity.getSessionId(), entity.getUserId(), null)).withRel("payment").withTitle("Payment reservation"),
                    linkTo(methodOn(ReservationController.class).cancelReservation(entity.getConferenceId(), entity.getSessionId(), entity.getUserId())).withRel("cancel").withTitle("Cancel reservation"),
                    linkTo(methodOn(ReservationController.class).getReservations(entity.getConferenceId(), entity.getSessionId())).withRel("collection").withTitle("Search reservations")
            );
        } catch (CannotProcessPaymentException e) {
            throw new RuntimeException(e);
        } catch (CannotProcessReservationException e) {
            throw new RuntimeException(e);
        }
    }

    public CollectionModel<EntityModel<Reservation>> toCollectionModel(String conferenceId, String sessionId, Iterable<? extends Reservation> entities) {
        List<EntityModel<Reservation>> intervenantModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(intervenantModel,
                linkTo(methodOn(ReservationController.class)
                        .getReservations(conferenceId, sessionId)).withRel("collection"));
    }
}
