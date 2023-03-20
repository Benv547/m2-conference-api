package fr.miage.conference.api.controller;

import fr.miage.conference.api.assembler.ReservationAssembler;
import fr.miage.conference.api.dto.BankCardInformationInput;
import fr.miage.conference.api.dto.ReservationInput;
import fr.miage.conference.bank.BankService;
import fr.miage.conference.bank.entity.BankCardInformation;
import fr.miage.conference.reservation.ReservationService;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessPaymentException;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollectionModel<EntityModel<Reservation>>> getReservations(@PathVariable String conferenceId, @PathVariable String sessionId) {

        List<Reservation> list = service.getReservations(conferenceId, sessionId);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toCollectionModel(conferenceId, sessionId, list));

    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EntityModel<Reservation>> getReservation(@PathVariable String conferenceId, @PathVariable String sessionId, @PathVariable String userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        Reservation reservation = service.getReservation(conferenceId, sessionId, userId);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(reservation));

    }

    @PostMapping(value = "/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EntityModel<Reservation>> createReservation(@PathVariable String conferenceId, @PathVariable String sessionId, @PathVariable String userId, @RequestBody @Valid ReservationInput input) throws CannotProcessReservationException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        Reservation reservation = new Reservation();
        reservation.setSessionId(sessionId);
        reservation.setConferenceId(conferenceId);
        reservation.setUserId(userId);
        reservation.setNbPlaces(input.getNbPlaces());
        reservation = service.createReservation(reservation);

        return ResponseEntity.ok(assembler.toModel(reservation));

    }

    @PostMapping(value = "/{userId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EntityModel> cancelReservation(@PathVariable String conferenceId, @PathVariable String sessionId, @PathVariable String userId) throws CannotProcessReservationException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        if (service.cancelReservation(conferenceId, sessionId, userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @PostMapping(value = "/{userId}/payment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EntityModel> paymentReservation(@PathVariable String conferenceId, @PathVariable String sessionId, @PathVariable String userId, @RequestBody @Valid BankCardInformationInput cardInformation) throws CannotProcessPaymentException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        if (service.paymentReservation(modelMapper.map(cardInformation, BankCardInformation.class), conferenceId, sessionId, userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
