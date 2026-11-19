package cl.corpaliv.aportes.dto;

/**
 * Misma forma que RegistroGuardado de la app (más motivo, estado y fecha, que Gson ignora).
 * Los textos que no aplican se devuelven como "" y no como null, porque en la app son String no nulos.
 */
public record AporteResponse(
        Long id,
        String tipo,
        String motivo,
        Long oportunidadId,
        String oportunidad,
        String nombre,
        String rut,
        String correo,
        String telefono,
        String tipoAporte,
        String direccion,
        String giro,
        String representante,
        String rutRepresentante,
        String estado,
        long fecha) {
}
