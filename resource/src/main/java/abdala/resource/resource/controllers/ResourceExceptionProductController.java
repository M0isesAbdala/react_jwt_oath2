package abdala.resource.resource.controllers;

import abdala.resource.resource.erros.interfaces.CustomResourceErrorProductApi;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice(annotations = CustomResourceErrorProductApi.class)
public class ResourceExceptionProductController {

    public enum ResourceErrorCode {

        GENERIC(0),
        CONFLICT(1),
        NOT_FOUND(2),
        VALIDATION_ERROR(3);

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

    public record GenericErrorFields(ResourceErrorCode code, String message, List<ErrorFields> fields) {
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(Exception ex) {
        return ResponseEntity
                .status(409)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericError(ResourceErrorCode.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(Exception ex) {
        return ResponseEntity
                .status(404)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericError(ResourceErrorCode.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {

        List<ErrorFields> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new ErrorFields(v.getPropertyPath().toString(), v.getMessage()))
                .toList();

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericErrorFields(ResourceErrorCode.VALIDATION_ERROR, ex.getMessage(), errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        List<ErrorFields> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(v -> new ErrorFields(v.getField(),
                                Objects.requireNonNullElse(v.getDefaultMessage(), "Valor inválido")
                        )
                ).toList();

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericErrorFields(ResourceErrorCode.VALIDATION_ERROR, ex.getMessage(), errors));
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
