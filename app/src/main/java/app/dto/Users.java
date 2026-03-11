package app.dto;

import app.entities.Role;

import java.util.Set;

public record Users(String username, String email, Set<String> roles) {
}
