package abdala.resource.resource.controllers;

import abdala.resource.resource.entities.Role;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class ResourceController {

    @GetMapping(path = "/roles", produces = "application/json")
    public String[] getRoles() {
        return Arrays.stream(Role.Roles.values()).map(Enum::name).toArray(String[]::new);
    }
}
