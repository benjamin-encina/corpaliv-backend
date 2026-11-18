package cl.corpaliv.oportunidades.dto;

/**
 * Misma forma que OportunidadModel de la app: Retrofit/Gson la lee sin cambios.
 * imagenUrl y contactoEmail pueden ser null (la app los trata como String?).
 */
public record OportunidadResponse(
        Long id,
        String titulo,
        String categoria,
        String descripcion,
        String impactoSocial,
        String imagenUrl,
        String contactoEmail,
        int metaFinanciamiento,
        int montoRecaudado) {
}
