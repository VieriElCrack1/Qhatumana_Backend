package pe.edu.cibertec.qhatumana.model.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "status", "jwt", "refreshToken"})
public record AuthResponse(
        String username,
        String message,
        String jwt,
        Boolean status,
        String refreshToken) {
}