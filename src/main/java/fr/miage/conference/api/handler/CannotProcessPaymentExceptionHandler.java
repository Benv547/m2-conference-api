package fr.miage.conference.api.handler;

import fr.miage.conference.reservation.exception.CannotProcessPaymentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CannotProcessPaymentExceptionHandler {

    @ExceptionHandler(CannotProcessPaymentException.class)
    public ResponseEntity<Object> handleConstraintViolationException(CannotProcessPaymentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
