package cl.corpaliv.auth.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> manejarApi(ApiException e) {
        return construir(e.getStatus(), e.getMessage(), e.getDetalles());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException e) {
        Map<String, String> detalles = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(f -> detalles.putIfAbsent(f.getField(), f.getDefaultMessage()));
        return construir(HttpStatus.BAD_REQUEST, "Datos inválidos", detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoIlegible(HttpMessageNotReadableException e) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud es ilegible o tiene valores no permitidos", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoParametro(MethodArgumentTypeMismatchException e) {
        return construir(HttpStatus.BAD_REQUEST, "Parámetro inválido: " + e.getName(), null);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje,
                                                    Map<String, String> detalles) {
        ErrorResponse cuerpo = new ErrorResponse(status.value(), status.getReasonPhrase(), mensaje,
                detalles, System.currentTimeMillis());
        return ResponseEntity.status(status).body(cuerpo);
    }
}
