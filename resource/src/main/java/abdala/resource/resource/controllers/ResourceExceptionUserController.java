package abdala.resource.resource.controllers;

import abdala.resource.resource.erros.EmailExistsError;
import abdala.resource.resource.erros.interfaces.CustomResourceErrorUserApi;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice(annotations = CustomResourceErrorUserApi.class)
public class ResourceExceptionUserController {

    public enum ResourceErrorCode {

        GENERIC(0),
        CONFLICT(1),
        NOT_FOUND(2);

        private final int code;

        ResourceErrorCode(int code) {
            this.code = code;
        }

        @JsonValue
        public int getCode() {
            return code;
        }
    }

    public record GenericError(ResourceErrorCode code, String message) {
    }

    public record ErrorFields(String field, String error) {
    }

    @ExceptionHandler({EmailExistsError.class, DataIntegrityViolationException.class})
    public ResponseEntity<?> handleDataIntegrity(Exception ex) {
        return ResponseEntity
                .status(409)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericError(ResourceErrorCode.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleNotFound(Exception ex) {
        return ResponseEntity
                .status(404)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericError(ResourceErrorCode.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericError(
                        ResourceErrorCode.GENERIC,
                        ex.getMessage()
                ));
    }
}
