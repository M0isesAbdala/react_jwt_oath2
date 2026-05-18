package abdala.authentication.service;

import abdala.authentication.dto.DTOTokenResponse;
import abdala.authentication.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${spring.security.oauth2.authorizationserver.issuer}")
    private String issuer;

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public DTOTokenResponse generateToken(User user){
        var now = Instant.now();
        var expiresIn = 10080L;

        var scopes = user.getRoles()
                .stream()
                .map((r) -> r.getName().name())
                .toList();

        var expire = now.plusSeconds(expiresIn);

        var claims = JwtClaimsSet.builder()
                .issuer(this.issuer)
                .subject(user.getEmail())
                .issuedAt(now)
                .notBefore(now)
                .expiresAt(expire)
                .claim("roles", String.join(" ", scopes))
                .build();


        return new DTOTokenResponse(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expiresIn, scopes);
    }

}
