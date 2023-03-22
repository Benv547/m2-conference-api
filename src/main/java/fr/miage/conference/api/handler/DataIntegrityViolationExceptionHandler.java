package fr.miage.conference.api.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DataIntegrityViolationExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

}
