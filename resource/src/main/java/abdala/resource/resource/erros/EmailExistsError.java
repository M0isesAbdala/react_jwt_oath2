package abdala.resource.resource.erros;

public class EmailExistsError extends RuntimeException {
    public EmailExistsError(String s) {
        super(s);
    }
}
