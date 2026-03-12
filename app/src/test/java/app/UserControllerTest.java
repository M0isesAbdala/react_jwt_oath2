package app;

import app.dto.DTOLoginRequest;
import app.dto.DTOLoginResponse;
import app.dto.DTOUser;
import app.dto.DTOUsers;
import app.entities.Role;
import app.entities.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import app.service.JwtService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
//@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc()
public class UserControllerTest {


    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestJwtFactory testJwtFactory;

    private User user;
    private Role userRole;
    private String token;

    @BeforeEach
    void setup() {
        String username = "admin";
        String email = "admin@example.com";
        String password = "12345";
        Long userId = 1L;

        userRole = new Role(Role.Roles.ADMIN);

        user = new User(userId, username, email, password, Set.of(userRole));

        DTOLoginResponse dtoLoginResponse = jwtService.generateToken(user);
        token = dtoLoginResponse.accessToken();

    }

    @Test
    void authenticate() throws Exception {

        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(userRole);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        DTOLoginRequest loginRequest = new DTOLoginRequest(user.getEmail(), user.getPassword());
        user.setPassword(encodedPassword);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getUser() throws Exception {

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(
                        get("/users/"+user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
               .andExpect(jsonPath("$.roles[0]").value(userRole.getName().name()))
                .andReturn();
    }

    @Test
    void getUsers() throws Exception {

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0]username").value(user.getUsername()))
                .andExpect(jsonPath("$[0]email").value(user.getEmail()))
                .andExpect(jsonPath("$[0]roles").value(userRole.getName().name()))
                .andReturn();
    }

    @Test
    void removeUser() throws Exception {
        user.setRoles(Set.of(new Role(Role.Roles.USER)));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(
                        delete("/users/"+user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andReturn();

        System.out.println("TOKEN: " + testJwtFactory.getToken());
    }
}
