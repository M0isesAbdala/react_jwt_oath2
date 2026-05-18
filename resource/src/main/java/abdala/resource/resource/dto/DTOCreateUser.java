package abdala.resource.resource.dto;

import java.util.Set;

public record DTOCreateUser(String username, String email, String password, Set<String> roles) {
}
