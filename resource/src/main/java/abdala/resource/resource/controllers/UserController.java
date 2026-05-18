package abdala.resource.resource.controllers;

import abdala.resource.resource.dto.DTOCreateUser;
import abdala.resource.resource.dto.DTOEditUser;
import abdala.resource.resource.dto.DTOUser;
import abdala.resource.resource.entities.Role;
import abdala.resource.resource.entities.User;
import abdala.resource.resource.erros.EmailExistsError;
import abdala.resource.resource.erros.UserNotFound;
import abdala.resource.resource.erros.interfaces.CustomResourceErrorUserApi;
import abdala.resource.resource.repository.RoleRepository;
import abdala.resource.resource.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@CustomResourceErrorUserApi
@SecurityRequirement(name = "oauth2")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> newUser(@RequestBody DTOCreateUser dto) {

        var basicRole = roleRepository.findByName(Role.Roles.USER);

        var userFromDb = userRepository.findByEmail(dto.email());
        if (userFromDb.isPresent()) {
            throw new EmailExistsError("E-mail já foi cadastrado!");
        }

        Set<Role> roles = new HashSet<>();
        for (String role : dto.roles()) {
            var q = Role.Roles.valueOf(role);
            var r = roleRepository.findByName(q);
            roles.add(r);
        }

        String password = passwordEncoder.encode(dto.password());

        User user = new User(null, dto.username(), dto.email(), password, roles);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Transactional
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> editUser(@RequestBody DTOEditUser dto) {

        var userFromDb = userRepository.findById(dto.id());

        if (userFromDb.isEmpty()) {
            throw new UserNotFound("Usuário não existe");
        }

        Set<Role> roles = new HashSet<>();
        for (String role : dto.roles()) {
            var q = Role.Roles.valueOf(role);
            var r = roleRepository.findByName(q);
            roles.add(r);
        }

        User user = new User(dto.id(), dto.username(), dto.email(), userFromDb.get().getPassword(), roles);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public Page<DTOUser> listUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> users = userRepository.findAll(pageable);

        return users.map(user ->
                objectMapper.convertValue(user, DTOUser.class)
        );
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOUser> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            var dtoUser = objectMapper.convertValue(user, DTOUser.class);
            return ResponseEntity.ok(dtoUser);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            if (user.get().getRoles().stream().anyMatch((r) -> r.getName() == Role.Roles.ADMIN)) {
                return ResponseEntity.status(403).build();
            }
            userRepository.delete(user.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
