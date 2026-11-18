package cl.corpaliv.aportes.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cl.corpaliv.aportes.exception.ApiException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AdminInterceptorTest {

    private static final String SECRETO = "secreto-de-pruebas-que-supera-los-32-bytes-ok";

    private final AdminInterceptor interceptor = new AdminInterceptor(new JwtVerificador(SECRETO));

    private String token(String rol) {
        return Jwts.builder()
                .subject("admin@corpaliv.cl")
                .claim("rol", rol)
                .signWith(Keys.hmacShaKeyFor(SECRETO.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    private boolean pasa(MockHttpServletRequest request) {
        return interceptor.preHandle(request, new MockHttpServletResponse(), new Object());
    }


    @Test
    void registrarUnAporteEsPublico() {
        assertTrue(pasa(new MockHttpServletRequest("POST", "/api/aportes")));
    }

    @Test
    void listarSinTokenLanza401() {
        ApiException error = assertThrows(ApiException.class,
                () -> pasa(new MockHttpServletRequest("GET", "/api/aportes")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

    @Test
    void listarConRolQueNoEsAdministradorLanza403() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/aportes");
        request.addHeader("Authorization", "Bearer " + token("VISITANTE"));

        ApiException error = assertThrows(ApiException.class, () -> pasa(request));

        assertEquals(HttpStatus.FORBIDDEN, error.getStatus());
    }

    @Test
    void administradorPuedeListarYCambiarEstado() {
        MockHttpServletRequest listar = new MockHttpServletRequest("GET", "/api/aportes");
        listar.addHeader("Authorization", "Bearer " + token("ADMINISTRADOR"));
        MockHttpServletRequest estado = new MockHttpServletRequest("PATCH", "/api/aportes/1/estado");
        estado.addHeader("Authorization", "Bearer " + token("ADMINISTRADOR"));

        assertTrue(pasa(listar));
        assertTrue(pasa(estado));
    }

    @Test
    void postEnOtraRutaNoEsPublico() {
        ApiException error = assertThrows(ApiException.class,
                () -> pasa(new MockHttpServletRequest("POST", "/api/aportes/1/estado")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

}
