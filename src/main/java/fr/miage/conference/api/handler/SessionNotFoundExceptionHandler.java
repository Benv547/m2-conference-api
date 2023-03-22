package fr.miage.conference.api.handler;

import fr.miage.conference.session.exception.SessionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SessionNotFoundExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<Object> handle(SessionNotFoundException e) {
        return ResponseEntity.status(404).build();
    }

}
