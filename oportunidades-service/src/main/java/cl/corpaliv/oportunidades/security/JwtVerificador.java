package cl.corpaliv.oportunidades.security;

import cl.corpaliv.oportunidades.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Verifica los tokens emitidos por auth-service (mismo secreto compartido). */
@Component
public class JwtVerificador {

    private final SecretKey clave;

    public JwtVerificador(@Value("${jwt.secret}") String secreto) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    public Claims leer(String token) {
        try {
            return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw ApiException.noAutorizado("Token inválido o expirado");
        }
    }
}
