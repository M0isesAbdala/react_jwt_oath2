package abdala.client.client.controllers;

import abdala.client.client.dto.DTOLoginRequest;
import abdala.client.client.dto.DTOTokenResponse;
import abdala.client.client.erros.AuthServerException;
import jakarta.servlet.http.HttpSession;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class AuthController {

    private final String authHost;
    private final WebClient webClient = WebClient.builder().build();

    public AuthController(Environment env) {
        this.authHost = env.getProperty("GATEWAY_URL") + ":" + env.getProperty("GATEWAY_PORT") + "/" + env.getProperty("AUTHENTICATION_NAME");
    }

    @GetMapping("/redirect-logout")
    public String logout() {
        return """
                    <script>
                        window.opener.postMessage(
                            { type: 'LOGOUT_SUCCESS' },
                            window.location.origin
                        );
                        window.close();
                    </script>
                """;
    }

    @GetMapping("/user/roles")
    public List<String> getUser(HttpSession session) {
       if(session.getAttribute("roles") != null){
          List<String> roles = (List<String>) session.getAttribute("roles");
          return roles.stream().map((r) ->r.replace("ROLE_", "")).toList();
       }
       return new ArrayList<>();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DTOLoginRequest body, HttpSession session) {

        DTOTokenResponse response = webClient.post()
                .uri(UriComponentsBuilder
                        .fromUriString(this.authHost)
                        .path("/auth/login")
                        .build()
                        .toUri()
                )
                .bodyValue(body)
                .exchangeToMono(r -> {

                    if (r.statusCode().is2xxSuccessful()) {
                        return r.bodyToMono(DTOTokenResponse.class);
                    }

                    return r.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(b ->
                                    Mono.error(
                                            new AuthServerException(r.statusCode(), b)
                                    )
                            );
                })
                .block();

        if (response == null || response.accessToken() == null) {
            throw new RuntimeException("token not returned");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                body.email(),
                null,
                response.scope().stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s)).toList()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        session.setAttribute("token", response.accessToken());
        session.setAttribute("roles", response.scope());

        return ResponseEntity.ok().build();
    }
}