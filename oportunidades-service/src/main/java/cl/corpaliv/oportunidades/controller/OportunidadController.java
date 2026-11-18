package cl.corpaliv.oportunidades.controller;

import cl.corpaliv.oportunidades.dto.OportunidadRequest;
import cl.corpaliv.oportunidades.dto.OportunidadResponse;
import cl.corpaliv.oportunidades.service.OportunidadService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadController {

    private final OportunidadService servicio;

    public OportunidadController(OportunidadService servicio) {
        this.servicio = servicio;
    }

    /** Público: la app lo consume sin iniciar sesión. Filtro opcional ?categoria= */
    @GetMapping
    public List<OportunidadResponse> listar(@RequestParam(required = false) String categoria) {
        return servicio.listar(categoria);
    }

    @GetMapping("/{id}")
    public OportunidadResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    /** Solo administrador. */
    @PostMapping
    public ResponseEntity<OportunidadResponse> crear(@Valid @RequestBody OportunidadRequest solicitud) {
        OportunidadResponse creada = servicio.crear(solicitud);
        return ResponseEntity.created(URI.create("/api/oportunidades/" + creada.id())).body(creada);
    }

    /** Solo administrador. */
    @PutMapping("/{id}")
    public OportunidadResponse actualizar(@PathVariable Long id,
                                          @Valid @RequestBody OportunidadRequest solicitud) {
        return servicio.actualizar(id, solicitud);
    }

    /** Solo administrador. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
