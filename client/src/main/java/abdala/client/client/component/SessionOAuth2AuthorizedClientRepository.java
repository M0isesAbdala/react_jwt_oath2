package abdala.client.client.component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.text.ParseException;
import java.util.List;

public class SessionOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(clientRegistrationId);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String registrationId = authorizedClient.getClientRegistration().getRegistrationId();
        session.setAttribute(registrationId, authorizedClient);
        String token = authorizedClient.getAccessToken().getTokenValue();
        session.setAttribute("token", token);
        System.out.println("TOKEN: " + authorizedClient.getAccessToken().getTokenValue());
        authorizedClient.getAccessToken().getScopes().forEach(System.out::println);
        principal.getAuthorities().forEach(System.out::println);

        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            List<String> scopes =(List<String>) claims.getClaim("roles");
            session.setAttribute("roles", scopes);

            scopes.forEach(System.out::println);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal.getName(),
                    null,
                    scopes.stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s)).toList()
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(clientRegistrationId);
            session.removeAttribute("token");
        }
    }
}