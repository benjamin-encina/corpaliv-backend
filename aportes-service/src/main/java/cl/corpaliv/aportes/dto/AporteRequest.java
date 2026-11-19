package cl.corpaliv.aportes.dto;

import cl.corpaliv.aportes.model.MotivoRegistro;
import cl.corpaliv.aportes.model.TipoRegistro;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Lo que envía el formulario de la app (persona o empresa).
 * motivo: APORTE (por defecto si no se envía) o POSTULACION ("Ingresar a Programa", solo personas).
 * Las reglas que dependen del tipo y del motivo las valida AporteService.
 * El RUT se valida solo por formato, igual que la app: 7 u 8 dígitos, guion y dígito o K.
 */
public record AporteRequest(
        @NotNull(message = "Indica si es PERSONA o EMPRESA") TipoRegistro tipo,
        MotivoRegistro motivo,
        Long oportunidadId,
        @NotBlank(message = "Campo obligatorio")
        @Size(max = 200, message = "Máximo 200 caracteres") String oportunidad,
        @NotBlank(message = "Campo obligatorio")
        @Size(min = 3, max = 150, message = "Debe tener entre 3 y 150 caracteres") String nombre,
        @NotBlank(message = "Campo obligatorio")
        @Pattern(regexp = "^\\d{7,8}-[\\dkK]$", message = "RUN/RUT inválido (ej: 45678987-3)") String rut,
        @NotBlank(message = "Campo obligatorio")
        @Email(message = "Correo inválido")
        @Size(max = 150, message = "Máximo 150 caracteres") String correo,
        @NotBlank(message = "Campo obligatorio")
        @Pattern(regexp = "^\\+56\\s?\\d{1,2}\\s?\\d{8}$", message = "Teléfono inválido (ej: +569 12345678)") String telefono,
        @Size(max = 60, message = "Máximo 60 caracteres") String tipoAporte,
        @Size(max = 200, message = "Máximo 200 caracteres") String direccion,
        @Size(max = 120, message = "Máximo 120 caracteres") String giro,
        @Size(max = 150, message = "Máximo 150 caracteres") String representante,
        @Pattern(regexp = "^$|^\\d{7,8}-[\\dkK]$", message = "RUT inválido (ej: 45678987-3)") String rutRepresentante) {
}
