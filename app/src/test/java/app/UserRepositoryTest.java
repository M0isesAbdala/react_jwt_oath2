package app;

import app.entities.Role;
import app.entities.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends AbstractProfileTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void createRoles(){
        Role admin = new Role(Role.Roles.ADMIN);
        Role user = new Role(Role.Roles.USER);
        roleRepository.save(admin);
        roleRepository.save(user);

        Optional<Role> resultAdmin = roleRepository.findById(Role.Roles.ADMIN.getRoleId());
        Optional<Role> resultUser = roleRepository.findById(Role.Roles.USER.getRoleId());

        assertTrue(resultAdmin.isPresent());
        assertEquals(Role.Roles.ADMIN.getRoleId(), resultAdmin.get().getId());
        assertEquals(Role.Roles.ADMIN.name(), resultAdmin.get().getName().name());

        assertTrue(resultUser.isPresent());
        assertEquals(Role.Roles.USER.getRoleId(), resultUser.get().getId());
        assertEquals(Role.Roles.USER.name(), resultUser.get().getName().name());

    }

    @Test
    void createUser() {

        String username = "test";
        String email = "test@example.com";
        String password = "12345";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = roleRepository.findById(Role.Roles.ADMIN.getRoleId()).orElseThrow();

        user.setRoles(Set.of(role));

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        assertEquals(email, result.get().getEmail());
        assertEquals(password, result.get().getPassword());

        assertTrue(user.getRoles()
                .stream()
                .anyMatch(r -> r.getName().name().equals(Role.Roles.ADMIN.name())));

        long userId = user.getId();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(newUser);
        });

        removeUser(userId);
    }

    void removeUser(Long id){
        userRepository.deleteById(id);
    }
}