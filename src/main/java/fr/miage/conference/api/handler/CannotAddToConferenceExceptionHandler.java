package fr.miage.conference.api.handler;

import fr.miage.conference.session.exception.CannotAddToConferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CannotAddToConferenceExceptionHandler {

    @ExceptionHandler(CannotAddToConferenceException.class)
    public ResponseEntity<?> handleConstraintViolationException(CannotAddToConferenceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
