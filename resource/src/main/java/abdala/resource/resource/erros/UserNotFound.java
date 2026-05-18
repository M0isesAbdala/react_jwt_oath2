package abdala.resource.resource.erros;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFound extends ResponseStatusException {
    public UserNotFound(String msg) {
        super(HttpStatus.NOT_FOUND, msg);
    }
}
