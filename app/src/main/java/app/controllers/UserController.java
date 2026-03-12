package app.controllers;

import app.dto.DTOCreateUser;
import app.dto.DTOUser;
import app.dto.DTOUsers;
import app.entities.Role;
import app.entities.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Void> newUser(@RequestBody DTOCreateUser dto) {

        var basicRole = roleRepository.findByName(Role.Roles.USER);

        var userFromDb = userRepository.findByEmail(dto.email());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = objectMapper.convertValue(dto, User.class);

        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<DTOUsers>> listUsers() {
        var users = userRepository.findAll();

        List<DTOUsers> DTOUsers = objectMapper.convertValue(users,
                objectMapper.getTypeFactory().constructCollectionType(List.class, DTOUsers.class)
        );

        return ResponseEntity.ok(DTOUsers);
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<DTOUser> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            var dtoUser = objectMapper.convertValue(user, DTOUser.class);
            return ResponseEntity.ok(dtoUser);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            if(user.get().getRoles().stream().allMatch((r) -> r.getName().name().equals("ADMIN"))){
                return ResponseEntity.status(403).build();
            }
            userRepository.delete(user.get());
            return ResponseEntity.ok().build();
        }

       return ResponseEntity.notFound().build();
    }
}
