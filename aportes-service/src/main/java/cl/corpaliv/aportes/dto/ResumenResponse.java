package cl.corpaliv.aportes.dto;

/**
 * Totales del Dashboard.
 * total = totalAportes + totalPostulaciones; totalAportes = totalPersonas + totalEmpresas
 * (personas y empresas que aportan, sin contar las postulaciones).
 */
public record ResumenResponse(
        long total,
        long totalAportes,
        long totalPostulaciones,
        long totalPersonas,
        long totalEmpresas,
        long pendientes) {
}
