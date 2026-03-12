package app.dto;

import java.util.List;

public record DTOJwtUsernameAndRoles(String userName, List<String> roles) {
}
