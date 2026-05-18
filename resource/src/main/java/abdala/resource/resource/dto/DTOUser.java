package abdala.resource.resource.dto;

import java.util.Set;

public record DTOUser(Long id, String username, String email, Set<String> roles) {
}
