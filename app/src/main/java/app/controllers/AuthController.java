package app.controllers;

import app.dto.DTOLoginRequest;
import app.dto.DTOLoginResponse;
import app.entities.Role;
import app.repository.UserRepository;
import app.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<DTOLoginResponse> login(@RequestBody DTOLoginRequest DTOLoginRequest) {

        var user = userRepository.findByEmail(DTOLoginRequest.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(DTOLoginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }

        return ResponseEntity.ok(jwtService.generateToken(user.get()));
    }
}