package cl.corpaliv.aportes.controller;

import cl.corpaliv.aportes.dto.AporteRequest;
import cl.corpaliv.aportes.dto.AporteResponse;
import cl.corpaliv.aportes.dto.EstadoRequest;
import cl.corpaliv.aportes.dto.ResumenResponse;
import cl.corpaliv.aportes.model.TipoRegistro;
import cl.corpaliv.aportes.service.AporteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aportes")
public class AporteController {

    private final AporteService servicio;

    public AporteController(AporteService servicio) {
        this.servicio = servicio;
    }

    /** Público: cualquier persona o empresa registra su aporte desde el formulario de la app. */
    @PostMapping
    public ResponseEntity<AporteResponse> crear(@Valid @RequestBody AporteRequest solicitud) {
        AporteResponse creado = servicio.crear(solicitud);
        return ResponseEntity.created(URI.create("/api/aportes/" + creado.id())).body(creado);
    }

    /** Solo administrador. Filtro opcional ?tipo=PERSONA o ?tipo=EMPRESA. */
    @GetMapping
    public List<AporteResponse> listar(@RequestParam(required = false) TipoRegistro tipo) {
        return servicio.listar(tipo);
    }

    /** Solo administrador. Totales para las tarjetas del Dashboard. */
    @GetMapping("/resumen")
    public ResumenResponse resumen() {
        return servicio.resumen();
    }

    /** Solo administrador. */
    @GetMapping("/{id}")
    public AporteResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    /** Solo administrador. */
    @PutMapping("/{id}")
    public AporteResponse actualizar(@PathVariable Long id, @Valid @RequestBody AporteRequest solicitud) {
        return servicio.actualizar(id, solicitud);
    }

    /** Solo administrador. Marca el aporte como CONTACTADO (o lo devuelve a PENDIENTE). */
    @PatchMapping("/{id}/estado")
    public AporteResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest solicitud) {
        return servicio.cambiarEstado(id, solicitud.estado());
    }

    /** Solo administrador. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
