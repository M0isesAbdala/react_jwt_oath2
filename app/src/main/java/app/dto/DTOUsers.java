package app.dto;


import java.util.Set;

public record DTOUsers(String username, String email, Set<String> roles) {
}
