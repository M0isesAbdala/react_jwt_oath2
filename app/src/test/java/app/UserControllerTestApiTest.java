package app;

import app.dto.DTOCreateUser;
import app.dto.DTOLoginRequest;
import app.entities.Role;
import app.entities.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class UserControllerTestApiTest extends AbstractProfileTest {

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ComponentsApiTest componentsApiTest;

    @Test
    void authenticate() throws Exception {

        String encodedPassword = passwordEncoder.encode(componentsApiTest.getJwtFactoryTest().getAdminUser().getPassword());
        DTOLoginRequest loginRequest = new DTOLoginRequest(componentsApiTest.getJwtFactoryTest().getAdminUser().getEmail(), componentsApiTest.getJwtFactoryTest().getAdminUser().getPassword());
        componentsApiTest.getJwtFactoryTest().getAdminUser().setPassword(encodedPassword);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(componentsApiTest.getJwtFactoryTest().getAdminUser());
        Mockito.when(userRepository.findByEmail(componentsApiTest.getJwtFactoryTest().getAdminUser().getEmail())).thenReturn(Optional.of(componentsApiTest.getJwtFactoryTest().getAdminUser()));

        componentsApiTest.getMockMvc().perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(componentsApiTest.getObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getUser() throws Exception {

        Mockito.when(userRepository.findById(componentsApiTest.getJwtFactoryTest().getAdminUser().getId())).thenReturn(Optional.of(componentsApiTest.getJwtFactoryTest().getAdminUser()));

        componentsApiTest.getMockMvc().perform(
                        get("/users/"+ componentsApiTest.getJwtFactoryTest().getAdminUser().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(componentsApiTest.getJwtFactoryTest().getAdminUser().getUsername()))
                .andExpect(jsonPath("$.email").value(componentsApiTest.getJwtFactoryTest().getAdminUser().getEmail()))
                .andExpect(jsonPath("$.roles[*]").value(hasItem(Role.Roles.ADMIN.name())))
                .andReturn();
    }

    @Test
    void getUsers() throws Exception {

        Mockito.when(userRepository.findAll()).thenReturn(List.of(componentsApiTest.getJwtFactoryTest().getAdminUser()));

        componentsApiTest.getMockMvc().perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]username").value(componentsApiTest.getJwtFactoryTest().getAdminUser().getUsername()))
                .andExpect(jsonPath("$[0]email").value(componentsApiTest.getJwtFactoryTest().getAdminUser().getEmail()))
                .andExpect(jsonPath("$[0]roles[*]").value(hasItem(Role.Roles.ADMIN.name())))
                .andReturn();
    }

    @Test
    void removeUser() throws Exception {

        Mockito.when(userRepository.findById(componentsApiTest.getJwtFactoryTest().getAdminUser().getId())).thenReturn(Optional.of(componentsApiTest.getJwtFactoryTest().getAdminUser()));

        componentsApiTest.getMockMvc().perform(
                        delete("/users/"+ componentsApiTest.getJwtFactoryTest().getAdminUser().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                ).andExpect(status().isForbidden())
                .andReturn();

        Mockito.when(userRepository.findById(componentsApiTest.getJwtFactoryTest().getAdminUser().getId())).thenReturn(Optional.of(componentsApiTest.getJwtFactoryTest().getUser()));

        componentsApiTest.getMockMvc().perform(
                        delete("/users/"+ componentsApiTest.getJwtFactoryTest().getAdminUser().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                ).andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void createUser() throws Exception {
        DTOCreateUser dto = new DTOCreateUser("test", "test@example.com", "12345");

        Mockito.when(roleRepository.findByName(Role.Roles.USER)).thenReturn(new Role(Role.Roles.USER));


        componentsApiTest.getMockMvc().perform(
                        post("/users")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(componentsApiTest.getObjectMapper().writeValueAsString(dto))
                ).andExpect(status().isCreated())
                .andReturn();

        Mockito.when(userRepository.findByEmail(componentsApiTest.getJwtFactoryTest().getAdminUser().getEmail())).thenReturn(Optional.of(componentsApiTest.getJwtFactoryTest().getAdminUser()));
        dto = new DTOCreateUser(componentsApiTest.getJwtFactoryTest().getAdminUser().getUsername(), componentsApiTest.getJwtFactoryTest().getAdminUser().getEmail(), "12345");

        componentsApiTest.getMockMvc().perform(
                        post("/users")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(componentsApiTest.getObjectMapper().writeValueAsString(dto))
                ).andExpect(status().isUnprocessableEntity())
                .andReturn();
    }
}
