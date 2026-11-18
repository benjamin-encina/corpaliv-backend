package cl.corpaliv.auth.security;

import cl.corpaliv.auth.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Emite y lee tokens JWT firmados con HMAC-SHA. */
@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${jwt.secret}") String secreto,
                      @Value("${jwt.expiracion-minutos:120}") long expiracionMinutos) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMinutos * 60_000L;
    }

    public String generar(Usuario usuario, long ahoraMs) {
        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol().name())
                .issuedAt(new Date(ahoraMs))
                .expiration(new Date(ahoraMs + expiracionMs))
                .signWith(clave)
                .compact();
    }

    /** Lanza JwtException si el token está alterado, mal formado o expirado. */
    public Claims leer(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
    }
}
