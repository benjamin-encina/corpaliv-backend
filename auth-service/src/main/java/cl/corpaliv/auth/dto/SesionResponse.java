package cl.corpaliv.auth.dto;

/** Respuesta del login: el token y los datos que la app guarda en su UsuarioSesion. */
public record SesionResponse(
        String token,
        String tipoToken,
        String nombre,
        String correo,
        String rol,
        String rolEtiqueta,
        long inicio) {
}
