package app.dto;

import java.util.List;

public record JwtUsernameAndRoles(String userName, List<String> roles) {
}
