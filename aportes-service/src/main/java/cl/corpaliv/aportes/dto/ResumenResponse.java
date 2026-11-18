package cl.corpaliv.aportes.dto;

/** Totales del Dashboard (totalPersonas y totalEmpresas como en DashboardUiState de la app). */
public record ResumenResponse(long total, long totalPersonas, long totalEmpresas, long pendientes) {
}
