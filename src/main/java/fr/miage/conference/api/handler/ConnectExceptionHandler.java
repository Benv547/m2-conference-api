package fr.miage.conference.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.ConnectException;

@RestControllerAdvice
public class ConnectExceptionHandler {

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> handleConnectExceptionHandler(ConnectException e) {
        return ResponseEntity.status(503).build();
    }

}
