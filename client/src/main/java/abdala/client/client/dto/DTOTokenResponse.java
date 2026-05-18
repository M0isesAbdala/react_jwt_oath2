package abdala.client.client.dto;

import java.util.List;

public record DTOTokenResponse(String accessToken, Long expiresIn, List<String> scope) {
}
