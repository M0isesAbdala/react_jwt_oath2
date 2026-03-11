package app.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
