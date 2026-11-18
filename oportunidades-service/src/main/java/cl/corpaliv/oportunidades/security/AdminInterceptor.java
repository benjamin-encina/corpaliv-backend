package cl.corpaliv.oportunidades.security;

import cl.corpaliv.oportunidades.exception.ApiException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Exige un token de ADMINISTRADOR en todos los endpoints salvo los públicos.
 * Público: solo las lecturas (GET). Crear, modificar y eliminar requieren administrador.
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtVerificador verificador;

    public AdminInterceptor(JwtVerificador verificador) {
        this.verificador = verificador;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || esPublico(request)) {
            return true;
        }

        String cabecera = request.getHeader("Authorization");
        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            throw ApiException.noAutorizado("Falta el token Bearer en la cabecera Authorization");
        }

        Claims claims = verificador.leer(cabecera.substring(7));
        if (!"ADMINISTRADOR".equals(claims.get("rol", String.class))) {
            throw ApiException.prohibido("Solo un administrador puede realizar esta operación");
        }
        return true;
    }

    private boolean esPublico(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }
}
