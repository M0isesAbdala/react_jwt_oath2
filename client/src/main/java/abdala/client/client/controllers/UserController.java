package abdala.client.client.controllers;

import abdala.client.client.dto.DTOCreateUser;
import abdala.client.client.dto.DTOEditUser;
import abdala.client.client.service.ApiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ApiService apiService;

    public UserController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String listUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return apiService.getUserApi().getUsers(page, size);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProduct(@PathVariable Long id) {
        return apiService.getUserApi().getUser(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody DTOCreateUser user) {
        return apiService.getUserApi().createUser(user);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editUser(@RequestBody DTOEditUser user) {
        return apiService.getUserApi().editUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return apiService.getUserApi().deleteUser(id);
    }
}
