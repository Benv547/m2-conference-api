package fr.miage.conference.api.handler;

import fr.miage.conference.reservation.exception.CannotCancelReservationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CannotCancelReservationExceptionHandler {

    @ExceptionHandler(CannotCancelReservationException.class)
    public ResponseEntity<Object> handle(CannotCancelReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
