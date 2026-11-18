package cl.corpaliv.oportunidades.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

/** Error de negocio con el código HTTP que debe recibir el cliente. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final Map<String, String> detalles;

    public ApiException(HttpStatus status, String mensaje, Map<String, String> detalles) {
        super(mensaje);
        this.status = status;
        this.detalles = detalles;
    }

    public ApiException(HttpStatus status, String mensaje) {
        this(status, mensaje, null);
    }

    public static ApiException noEncontrado(String mensaje) {
        return new ApiException(HttpStatus.NOT_FOUND, mensaje);
    }

    public static ApiException noAutorizado(String mensaje) {
        return new ApiException(HttpStatus.UNAUTHORIZED, mensaje);
    }

    public static ApiException prohibido(String mensaje) {
        return new ApiException(HttpStatus.FORBIDDEN, mensaje);
    }

    public static ApiException solicitudInvalida(String mensaje, Map<String, String> detalles) {
        return new ApiException(HttpStatus.BAD_REQUEST, mensaje, detalles);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getDetalles() {
        return detalles;
    }
}
