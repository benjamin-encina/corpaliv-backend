package cl.corpaliv.aportes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.corpaliv.aportes.dto.AporteRequest;
import cl.corpaliv.aportes.dto.AporteResponse;
import cl.corpaliv.aportes.dto.ResumenResponse;
import cl.corpaliv.aportes.exception.ApiException;
import cl.corpaliv.aportes.model.Aporte;
import cl.corpaliv.aportes.model.EstadoContacto;
import cl.corpaliv.aportes.model.TipoRegistro;
import cl.corpaliv.aportes.repository.AporteRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AporteServiceTest {

    private static final long AHORA = 1_700_000_000_000L;

    @Mock
    private AporteRepository repositorio;

    private AporteService servicio;

    @BeforeEach
    void preparar() {
        Clock reloj = Clock.fixed(Instant.ofEpochMilli(AHORA), ZoneOffset.UTC);
        servicio = new AporteService(repositorio, reloj);
    }

    private AporteRequest persona(String tipoAporte) {
        return new AporteRequest(TipoRegistro.PERSONA, 1L, "Taller de Preparación Laboral Alba Lab",
                "  Camila Silva ", "12345678-9", "camila@mail.cl", "+569 12345678", tipoAporte,
                null, null, null, null);
    }

    private AporteRequest empresa(String giro) {
        return new AporteRequest(TipoRegistro.EMPRESA, 2L, "Equipamiento Escuela Jean Van Dijk",
                "Constructora Sur SpA", "76123456-k", "contacto@sur.cl", "+56 123456789", null,
                "Av. Collao 1202, Concepción", giro, "Pedro Rojas", "11222333-k");
    }

    private Aporte guardado(Long id, TipoRegistro tipo) {
        Aporte a = new Aporte();
        a.setId(id);
        a.setTipo(tipo);
        a.setOportunidad("Taller");
        a.setNombre("Camila Silva");
        a.setRut("12345678-9");
        a.setCorreo("camila@mail.cl");
        a.setTelefono("+569 12345678");
        a.setTipoAporte("Mensual");
        a.setFechaRegistro(AHORA);
        return a;
    }

    private void guardarDevolviendoId(Long id) {
        when(repositorio.save(any(Aporte.class))).thenAnswer(inv -> {
            Aporte a = inv.getArgument(0);
            a.setId(id);
            return a;
        });
    }

    @Test
    void crearPersonaQueAportaGuardaPendienteConFecha() {
        guardarDevolviendoId(1L);

        AporteResponse respuesta = servicio.crear(persona("Mensual"));

        assertEquals(1L, respuesta.id());
        assertEquals("PERSONA", respuesta.tipo());
        assertEquals("Camila Silva", respuesta.nombre());
        assertEquals("Mensual", respuesta.tipoAporte());
        assertEquals("PENDIENTE", respuesta.estado());
        assertEquals(AHORA, respuesta.fecha());
        assertEquals("", respuesta.giro());
    }

    @Test
    void crearPersonaSinFrecuenciaValidaLanza400() {
        ApiException error = assertThrows(ApiException.class, () -> servicio.crear(persona("Cada semana")));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
        assertTrue(error.getDetalles().containsKey("tipoAporte"));
        verify(repositorio, never()).save(any());
    }

    @Test
    void crearEmpresaCompletaNormalizaRutYFijaTipoDeAporte() {
        guardarDevolviendoId(2L);

        AporteResponse respuesta = servicio.crear(empresa("Construcción"));

        assertEquals("EMPRESA", respuesta.tipo());
        assertEquals("Aporte empresarial", respuesta.tipoAporte());
        assertEquals("76123456-K", respuesta.rut());
        assertEquals("11222333-K", respuesta.rutRepresentante());
        assertEquals("Construcción", respuesta.giro());
    }

    @Test
    void crearEmpresaSinGiroLanza400() {
        ApiException error = assertThrows(ApiException.class, () -> servicio.crear(empresa(" ")));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
        assertTrue(error.getDetalles().containsKey("giro"));
    }

    @Test
    void listarSinFiltroEntregaTodos() {
        when(repositorio.findAllByOrderByFechaRegistroDesc())
                .thenReturn(List.of(guardado(2L, TipoRegistro.EMPRESA), guardado(1L, TipoRegistro.PERSONA)));

        assertEquals(2, servicio.listar(null).size());
    }

    @Test
    void listarConTipoUsaElFiltro() {
        when(repositorio.findByTipoOrderByFechaRegistroDesc(TipoRegistro.PERSONA))
                .thenReturn(List.of(guardado(1L, TipoRegistro.PERSONA)));

        List<AporteResponse> lista = servicio.listar(TipoRegistro.PERSONA);

        assertEquals(1, lista.size());
        assertEquals("PERSONA", lista.get(0).tipo());
    }

    @Test
    void obtenerInexistenteLanza404() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        ApiException error = assertThrows(ApiException.class, () -> servicio.obtener(99L));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
    }

    @Test
    void actualizarCambiaLosDatosYConservaLaFecha() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(guardado(1L, TipoRegistro.PERSONA)));
        when(repositorio.save(any(Aporte.class))).thenAnswer(inv -> inv.getArgument(0));

        AporteResponse respuesta = servicio.actualizar(1L, persona("Anual"));

        assertEquals("Anual", respuesta.tipoAporte());
        assertEquals(AHORA, respuesta.fecha());
    }

    @Test
    void cambiarEstadoMarcaComoContactado() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(guardado(1L, TipoRegistro.PERSONA)));
        when(repositorio.save(any(Aporte.class))).thenAnswer(inv -> inv.getArgument(0));

        AporteResponse respuesta = servicio.cambiarEstado(1L, EstadoContacto.CONTACTADO);

        assertEquals("CONTACTADO", respuesta.estado());
    }

    @Test
    void eliminarInexistenteLanza404SinBorrar() {
        when(repositorio.existsById(9L)).thenReturn(false);

        ApiException error = assertThrows(ApiException.class, () -> servicio.eliminar(9L));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        verify(repositorio, never()).deleteById(any());
    }

    @Test
    void eliminarExistenteBorraPorId() {
        when(repositorio.existsById(1L)).thenReturn(true);

        servicio.eliminar(1L);

        verify(repositorio).deleteById(1L);
    }

    @Test
    void resumenEntregaLosTotalesDelDashboard() {
        when(repositorio.count()).thenReturn(3L);
        when(repositorio.countByTipo(TipoRegistro.PERSONA)).thenReturn(2L);
        when(repositorio.countByTipo(TipoRegistro.EMPRESA)).thenReturn(1L);
        when(repositorio.countByEstado(EstadoContacto.PENDIENTE)).thenReturn(2L);

        ResumenResponse resumen = servicio.resumen();

        assertEquals(3L, resumen.total());
        assertEquals(2L, resumen.totalPersonas());
        assertEquals(1L, resumen.totalEmpresas());
        assertEquals(2L, resumen.pendientes());
    }
}
