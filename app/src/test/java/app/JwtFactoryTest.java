package app;

import app.dto.DTOLoginResponse;
import app.entities.Role;
import app.entities.User;
import app.service.JwtService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class JwtFactoryTest {

    private final JwtService jwtService;

    @Getter
    private User adminUser;
    @Getter
    private User user;
    @Getter
    private String adminToken;
    @Getter
    private String userToken;

    public JwtFactoryTest(JwtService jwtService) {
        this.jwtService = jwtService;
        this.generateAdminToken();
    }

    private void generateAdminToken() {
        String username = "admin";
        String email = "admin@example.com";
        String password = "12345";
        long userId = 1L;

        adminUser = new User(userId, username, email, password, Set.of(new Role(Role.Roles.ADMIN)));

        DTOLoginResponse dtoLoginResponse = jwtService.generateToken(adminUser);
        adminToken = dtoLoginResponse.accessToken();

        username = "test";
        email = "test@example.com";
        password = "12345";
        userId = 2L;

        user = new User(userId, username, email, password, Set.of(new Role(Role.Roles.USER)));

        dtoLoginResponse = jwtService.generateToken(user);
        userToken = dtoLoginResponse.accessToken();
    }
}