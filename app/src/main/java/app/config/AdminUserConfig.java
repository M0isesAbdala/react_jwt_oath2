package app.config;

import app.entities.Role;
import app.entities.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Configuration
@Profile("!test")
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        for (Role.Roles roleName : Role.Roles.values()) {

            if (roleRepository.findById(roleName.getRoleId()).isEmpty()) {
                Role role = new Role(roleName);
                roleRepository.save(role);
            }
        }

        Optional<User> userAdmin = userRepository.findByEmail("admin@example.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    var roleAdmin = roleRepository.findByName(Role.Roles.ADMIN);

                    var user = new User();
                    user.setUsername("admin");
                    user.setEmail("admin@example.com");
                    user.setPassword(passwordEncoder.encode("12345"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
