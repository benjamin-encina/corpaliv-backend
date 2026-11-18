package cl.corpaliv.oportunidades.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.corpaliv.oportunidades.dto.OportunidadRequest;
import cl.corpaliv.oportunidades.dto.OportunidadResponse;
import cl.corpaliv.oportunidades.exception.ApiException;
import cl.corpaliv.oportunidades.model.Oportunidad;
import cl.corpaliv.oportunidades.repository.OportunidadRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class OportunidadServiceTest {

    @Mock
    private OportunidadRepository repositorio;

    private OportunidadService servicio;

    @BeforeEach
    void preparar() {
        servicio = new OportunidadService(repositorio);
    }

    private Oportunidad ejemplo(Long id) {
        Oportunidad o = new Oportunidad("Taller Alba Lab", "RSE Empresarial", "Descripción", "Impacto",
                null, "rse@corpaliv-demo.cl", 5_000_000, 1_200_000);
        o.setId(id);
        return o;
    }

    private OportunidadRequest solicitud(String titulo, String imagenUrl) {
        return new OportunidadRequest(titulo, "Donación Directa", "Descripción", "Impacto",
                imagenUrl, "", 3_000_000, 250_000);
    }

    @Test
    void listarSinFiltroEntregaTodas() {
        when(repositorio.findAllByOrderByIdAsc()).thenReturn(List.of(ejemplo(1L), ejemplo(2L)));

        List<OportunidadResponse> lista = servicio.listar(null);

        assertEquals(2, lista.size());
        assertEquals("Taller Alba Lab", lista.get(0).titulo());
    }

    @Test
    void listarConCategoriaUsaElFiltro() {
        when(repositorio.findByCategoriaIgnoreCaseOrderByIdAsc("Donación Directa"))
                .thenReturn(List.of(ejemplo(2L)));

        List<OportunidadResponse> lista = servicio.listar(" Donación Directa ");

        assertEquals(1, lista.size());
        assertEquals(2L, lista.get(0).id());
    }

    @Test
    void obtenerInexistenteLanza404() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        ApiException error = assertThrows(ApiException.class, () -> servicio.obtener(99L));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
    }

    @Test
    void crearGuardaYDevuelveLaOportunidadConId() {
        when(repositorio.save(any(Oportunidad.class))).thenAnswer(inv -> {
            Oportunidad o = inv.getArgument(0);
            o.setId(5L);
            return o;
        });

        OportunidadResponse creada = servicio.crear(solicitud("  Equipamiento escuela  ", "  "));

        assertEquals(5L, creada.id());
        assertEquals("Equipamiento escuela", creada.titulo());
        assertNull(creada.imagenUrl());
        assertNull(creada.contactoEmail());
    }

    @Test
    void actualizarModificaLosCamposDeLaExistente() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(ejemplo(1L)));
        when(repositorio.save(any(Oportunidad.class))).thenAnswer(inv -> inv.getArgument(0));

        OportunidadResponse actualizada = servicio.actualizar(1L, solicitud("Nuevo título", "https://img.cl/a.png"));

        assertEquals(1L, actualizada.id());
        assertEquals("Nuevo título", actualizada.titulo());
        assertEquals("https://img.cl/a.png", actualizada.imagenUrl());
        assertEquals(3_000_000, actualizada.metaFinanciamiento());
    }

    @Test
    void eliminarExistenteBorraPorId() {
        when(repositorio.existsById(1L)).thenReturn(true);

        servicio.eliminar(1L);

        verify(repositorio).deleteById(1L);
    }

    @Test
    void eliminarInexistenteLanza404SinBorrar() {
        when(repositorio.existsById(9L)).thenReturn(false);

        ApiException error = assertThrows(ApiException.class, () -> servicio.eliminar(9L));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        verify(repositorio, never()).deleteById(any());
    }
}
