package cl.corpaliv.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Ingresa tu correo") String correo,
        @NotBlank(message = "Ingresa tu contraseña") String clave) {
}
