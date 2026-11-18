package cl.corpaliv.auth.controller;

import cl.corpaliv.auth.dto.LoginRequest;
import cl.corpaliv.auth.dto.SesionResponse;
import cl.corpaliv.auth.dto.UsuarioResponse;
import cl.corpaliv.auth.exception.ApiException;
import cl.corpaliv.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService servicio;

    public AuthController(AuthService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/login")
    public SesionResponse login(@Valid @RequestBody LoginRequest solicitud) {
        return servicio.login(solicitud);
    }

    @GetMapping("/me")
    public UsuarioResponse me(@RequestHeader(value = "Authorization", required = false) String autorizacion) {
        if (autorizacion == null || !autorizacion.startsWith("Bearer ")) {
            throw ApiException.noAutorizado("Falta el token Bearer en la cabecera Authorization");
        }
        return servicio.perfil(autorizacion.substring(7));
    }
}
