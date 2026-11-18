package cl.corpaliv.oportunidades.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

/** Cuerpo JSON uniforme para todos los errores de la API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String mensaje,
        Map<String, String> detalles,
        long timestamp) {
}
