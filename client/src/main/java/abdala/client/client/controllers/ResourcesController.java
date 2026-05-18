package abdala.client.client.controllers;

import abdala.client.client.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.Role;
import java.util.Arrays;

@RestController
public class ResourcesController {

    private final ApiService apiService;

    public ResourcesController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(path = "/roles", produces = "application/json")
    public String getRoles() {
        return apiService.getResourceApi().getRoles();
    }
}
