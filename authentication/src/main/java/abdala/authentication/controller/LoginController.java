package abdala.authentication.controller;

import abdala.authentication.dto.DTOLoginRequest;
import abdala.authentication.dto.DTOTokenResponse;
import abdala.authentication.erros.CustomErrorBadCredentials;
import abdala.authentication.repository.UserRepository;
import abdala.authentication.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping(path = "/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<DTOTokenResponse> authLogin(@RequestBody DTOLoginRequest DTOLoginRequest) {

        var user = userRepository.findByEmail(DTOLoginRequest.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(DTOLoginRequest, passwordEncoder)) {
            throw new CustomErrorBadCredentials("User or password is invalid!");
        }

        return ResponseEntity.ok(jwtService.generateToken(user.get()));
    }

}