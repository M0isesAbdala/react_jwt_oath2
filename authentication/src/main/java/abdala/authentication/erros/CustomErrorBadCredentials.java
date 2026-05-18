package abdala.authentication.erros;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;

public class CustomErrorBadCredentials extends BadCredentialsException {

    public CustomErrorBadCredentials(@Nullable String msg) {
        super(msg);
    }
}
