package abdala.client.client.dto;

import java.util.Set;

public record DTOCreateUser(String username, String email, String password, Set<String> roles) {
}
