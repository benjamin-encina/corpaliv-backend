package cl.corpaliv.oportunidades.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cl.corpaliv.oportunidades.exception.ApiException;
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
    void lasLecturasSonPublicas() {
        assertTrue(pasa(new MockHttpServletRequest("GET", "/api/oportunidades")));
    }

    @Test
    void crearSinTokenLanza401() {
        ApiException error = assertThrows(ApiException.class,
                () -> pasa(new MockHttpServletRequest("POST", "/api/oportunidades")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

    @Test
    void crearConRolQueNoEsAdministradorLanza403() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/oportunidades");
        request.addHeader("Authorization", "Bearer " + token("VISITANTE"));

        ApiException error = assertThrows(ApiException.class, () -> pasa(request));

        assertEquals(HttpStatus.FORBIDDEN, error.getStatus());
    }

    @Test
    void administradorPuedeEliminar() {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/api/oportunidades/1");
        request.addHeader("Authorization", "Bearer " + token("ADMINISTRADOR"));

        assertTrue(pasa(request));
    }

    @Test
    void tokenAlteradoLanza401() {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/api/oportunidades/1");
        request.addHeader("Authorization", "Bearer token.alterado.xx");

        ApiException error = assertThrows(ApiException.class, () -> pasa(request));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

}
