package abdala.client.client.dto;

import java.util.Set;

public record DTOEditUser(Long id, String username, String email, Set<String> roles) {
}
