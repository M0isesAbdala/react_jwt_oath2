package app.dto;

import java.util.Set;

public record DTOUser(String username, String email, Set<String> roles) {
}
