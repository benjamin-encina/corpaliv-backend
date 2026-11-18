package cl.corpaliv.auth.service;

import cl.corpaliv.auth.dto.LoginRequest;
import cl.corpaliv.auth.dto.SesionResponse;
import cl.corpaliv.auth.dto.UsuarioResponse;
import cl.corpaliv.auth.exception.ApiException;
import cl.corpaliv.auth.model.Usuario;
import cl.corpaliv.auth.repository.UsuarioRepository;
import cl.corpaliv.auth.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String MENSAJE_CREDENCIALES = "Correo o contraseña incorrectos";

    private final UsuarioRepository repositorio;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UsuarioRepository repositorio, PasswordEncoder encoder, JwtService jwt) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    /** El correo no distingue mayúsculas; la contraseña sí. */
    public SesionResponse login(LoginRequest solicitud) {
        Usuario usuario = repositorio.findByCorreoIgnoreCase(solicitud.correo().trim())
                .orElseThrow(() -> ApiException.noAutorizado(MENSAJE_CREDENCIALES));

        if (!encoder.matches(solicitud.clave(), usuario.getClaveHash())) {
            throw ApiException.noAutorizado(MENSAJE_CREDENCIALES);
        }

        long ahora = System.currentTimeMillis();
        return new SesionResponse(
                jwt.generar(usuario, ahora),
                "Bearer",
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().name(),
                usuario.getRol().getEtiqueta(),
                ahora);
    }

    public UsuarioResponse perfil(String token) {
        Claims claims;
        try {
            claims = jwt.leer(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw ApiException.noAutorizado("Token inválido o expirado");
        }

        Usuario usuario = repositorio.findByCorreoIgnoreCase(claims.getSubject())
                .orElseThrow(() -> ApiException.noAutorizado("La cuenta ya no existe"));

        return new UsuarioResponse(
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().name(),
                usuario.getRol().getEtiqueta());
    }
}
