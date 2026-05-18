package abdala.client.client.controllers;

import abdala.client.client.erros.AuthServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthServerException.class)
    public ResponseEntity<?> handle(AuthServerException ex) {
        return ResponseEntity.status(ex.getStatus()).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(ex.getBody());
    }
}