package abdala.authentication.controller;

import abdala.authentication.dto.DTOCustomBadCredentialsErros;
import abdala.authentication.erros.CustomErrorBadCredentials;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomErrorBadCredentials.class)
    public ResponseEntity<DTOCustomBadCredentialsErros> handle(CustomErrorBadCredentials ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DTOCustomBadCredentialsErros(ex.getMessage()));
    }
}
