package fr.miage.conference.api.controller;

import fr.miage.conference.api.assembler.ReservationAssembler;
import fr.miage.conference.api.dto.ReservationInput;
import fr.miage.conference.reservation.ReservationService;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/conferences/{conferenceId}/sessions/{sessionId}/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    @EJB
    private ReservationService service;

    @EJB
    private ReservationAssembler assembler;

    private static ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reservation>>> getReservations(@PathVariable String conferenceId, @PathVariable String sessionId) {

        List<Reservation> list = service.getReservations(conferenceId, sessionId);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toCollectionModel(list));

    }

    @PostMapping(value = "/{userId}")
    public ResponseEntity<EntityModel<Reservation>> createReservation(@PathVariable String conferenceId, @PathVariable String sessionId, @PathVariable String userId, @RequestBody @Valid ReservationInput input) throws CannotProcessReservationException {

        Reservation reservation = new Reservation();
        reservation.setSessionId(sessionId);
        reservation.setConferenceId(conferenceId);
        reservation.setUserId(userId);
        reservation.setNbPlaces(input.getNbPlaces());
        reservation = service.createReservation(reservation);

        return ResponseEntity.ok(assembler.toModel(reservation));

    }

}
