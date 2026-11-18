package cl.corpaliv.oportunidades.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OportunidadRequest(
        @NotBlank(message = "Campo obligatorio")
        @Size(min = 3, max = 200, message = "Debe tener entre 3 y 200 caracteres") String titulo,
        @NotBlank(message = "Campo obligatorio")
        @Size(max = 80, message = "Máximo 80 caracteres") String categoria,
        @NotBlank(message = "Campo obligatorio")
        @Size(max = 1000, message = "Máximo 1000 caracteres") String descripcion,
        @NotBlank(message = "Campo obligatorio")
        @Size(max = 500, message = "Máximo 500 caracteres") String impactoSocial,
        @Size(max = 500, message = "Máximo 500 caracteres") String imagenUrl,
        @Email(message = "Correo inválido")
        @Size(max = 150, message = "Máximo 150 caracteres") String contactoEmail,
        @Min(value = 1, message = "La meta debe ser mayor a 0") int metaFinanciamiento,
        @Min(value = 0, message = "El monto no puede ser negativo") int montoRecaudado) {
}
