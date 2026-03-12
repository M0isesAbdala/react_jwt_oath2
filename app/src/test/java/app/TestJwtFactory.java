package app;

import app.dto.DTOLoginResponse;
import app.entities.Role;
import app.entities.User;
import app.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestJwtFactory {

    private final JwtService jwtService;

    private User user;
    private Role userRole;
    private String token;

    public TestJwtFactory(JwtService jwtService) {
        this.jwtService = jwtService;
        this.generateAdminToken();
        System.out.println("CONSTRUIU O TOKEN");
    }

    private void generateAdminToken() {
        String username = "admin";
        String email = "admin@example.com";
        String password = "12345";
        Long userId = 1L;

        userRole = new Role(Role.Roles.ADMIN);

        user = new User(userId, username, email, password, Set.of(userRole));

        DTOLoginResponse dtoLoginResponse = jwtService.generateToken(user);
        token = dtoLoginResponse.accessToken();
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Role getUserRole() {
        return userRole;
    }
}