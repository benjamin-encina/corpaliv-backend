package cl.corpaliv.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import cl.corpaliv.auth.dto.LoginRequest;
import cl.corpaliv.auth.dto.SesionResponse;
import cl.corpaliv.auth.dto.UsuarioResponse;
import cl.corpaliv.auth.exception.ApiException;
import cl.corpaliv.auth.model.Rol;
import cl.corpaliv.auth.model.Usuario;
import cl.corpaliv.auth.repository.UsuarioRepository;
import cl.corpaliv.auth.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String SECRETO = "secreto-de-pruebas-que-supera-los-32-bytes-ok";

    @Mock
    private UsuarioRepository repositorio;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtService jwt = new JwtService(SECRETO, 60);

    private AuthService servicio;
    private Usuario admin;

    @BeforeEach
    void preparar() {
        servicio = new AuthService(repositorio, encoder, jwt);
        admin = new Usuario("Administrador CORPALIV", "admin@corpaliv.cl", encoder.encode("admin"),
                Rol.ADMINISTRADOR);
    }

    @Test
    void loginCorrectoEntregaTokenYRol() {
        when(repositorio.findByCorreoIgnoreCase("admin@corpaliv.cl")).thenReturn(Optional.of(admin));

        SesionResponse respuesta = servicio.login(new LoginRequest("admin@corpaliv.cl", "admin"));

        assertFalse(respuesta.token().isBlank());
        assertEquals("ADMINISTRADOR", respuesta.rol());
        assertEquals("Administrador", respuesta.rolEtiqueta());
        assertEquals("Administrador CORPALIV", respuesta.nombre());
    }

    @Test
    void loginConClaveIncorrectaLanza401() {
        when(repositorio.findByCorreoIgnoreCase("admin@corpaliv.cl")).thenReturn(Optional.of(admin));

        ApiException error = assertThrows(ApiException.class,
                () -> servicio.login(new LoginRequest("admin@corpaliv.cl", "otra")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

    @Test
    void loginConCorreoInexistenteLanza401() {
        when(repositorio.findByCorreoIgnoreCase("nadie@corpaliv.cl")).thenReturn(Optional.empty());

        ApiException error = assertThrows(ApiException.class,
                () -> servicio.login(new LoginRequest("nadie@corpaliv.cl", "admin")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

    @Test
    void perfilConTokenValidoEntregaLosDatosDeLaCuenta() {
        String token = jwt.generar(admin, System.currentTimeMillis());
        when(repositorio.findByCorreoIgnoreCase("admin@corpaliv.cl")).thenReturn(Optional.of(admin));

        UsuarioResponse perfil = servicio.perfil(token);

        assertEquals("admin@corpaliv.cl", perfil.correo());
        assertEquals("ADMINISTRADOR", perfil.rol());
    }

    @Test
    void perfilConTokenAlteradoLanza401() {
        ApiException error = assertThrows(ApiException.class, () -> servicio.perfil("token.invalido.xx"));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }
}
