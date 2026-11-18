package cl.corpaliv.aportes.dto;

import cl.corpaliv.aportes.model.EstadoContacto;
import jakarta.validation.constraints.NotNull;

public record EstadoRequest(@NotNull(message = "Indica PENDIENTE o CONTACTADO") EstadoContacto estado) {
}
