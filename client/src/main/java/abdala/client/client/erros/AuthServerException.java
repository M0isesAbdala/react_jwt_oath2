package abdala.client.client.erros;

import org.springframework.http.HttpStatusCode;

public class AuthServerException extends RuntimeException {

    private final HttpStatusCode status;
    private final String body;

    public AuthServerException(HttpStatusCode status, String body) {
        super(body);
        this.status = status;
        this.body = body;
    }

    public HttpStatusCode getStatus() { return status; }
    public String getBody() { return body; }
}