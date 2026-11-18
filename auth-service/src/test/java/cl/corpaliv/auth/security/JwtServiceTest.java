package cl.corpaliv.auth.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cl.corpaliv.auth.model.Rol;
import cl.corpaliv.auth.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRETO = "secreto-de-pruebas-que-supera-los-32-bytes-ok";

    private final Usuario admin = new Usuario("Administrador CORPALIV", "admin@corpaliv.cl", "hash",
            Rol.ADMINISTRADOR);

    @Test
    void tokenGeneradoSePuedeLeerConSusDatos() {
        JwtService servicio = new JwtService(SECRETO, 60);

        String token = servicio.generar(admin, System.currentTimeMillis());
        Claims claims = servicio.leer(token);

        assertEquals("admin@corpaliv.cl", claims.getSubject());
        assertEquals("ADMINISTRADOR", claims.get("rol", String.class));
    }

    @Test
    void tokenExpiradoSeRechaza() {
        JwtService servicio = new JwtService(SECRETO, 60);

        String token = servicio.generar(admin, 0L);

        assertThrows(JwtException.class, () -> servicio.leer(token));
    }

    @Test
    void tokenFirmadoConOtroSecretoSeRechaza() {
        JwtService emisor = new JwtService("otro-secreto-distinto-que-tambien-supera-32-bytes", 60);
        JwtService lector = new JwtService(SECRETO, 60);

        String token = emisor.generar(admin, System.currentTimeMillis());

        assertThrows(JwtException.class, () -> lector.leer(token));
    }
}
