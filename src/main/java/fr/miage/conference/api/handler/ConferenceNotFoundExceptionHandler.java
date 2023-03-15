package fr.miage.conference.api.handler;

import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConferenceNotFoundExceptionHandler {

    @ExceptionHandler(ConferenceNotFoundException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConferenceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
