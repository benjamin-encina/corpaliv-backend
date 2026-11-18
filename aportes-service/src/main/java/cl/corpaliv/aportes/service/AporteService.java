package cl.corpaliv.aportes.service;

import cl.corpaliv.aportes.dto.AporteRequest;
import cl.corpaliv.aportes.dto.AporteResponse;
import cl.corpaliv.aportes.dto.ResumenResponse;
import cl.corpaliv.aportes.exception.ApiException;
import cl.corpaliv.aportes.model.Aporte;
import cl.corpaliv.aportes.model.EstadoContacto;
import cl.corpaliv.aportes.model.TipoRegistro;
import cl.corpaliv.aportes.repository.AporteRepository;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AporteService {

    // Mismas frecuencias que el desplegable de la app
    private static final List<String> FRECUENCIAS = List.of("Donación única", "Mensual", "Anual");
    private static final String APORTE_EMPRESARIAL = "Aporte empresarial";

    private final AporteRepository repositorio;
    private final Clock reloj;

    public AporteService(AporteRepository repositorio, Clock reloj) {
        this.repositorio = repositorio;
        this.reloj = reloj;
    }

    @Transactional
    public AporteResponse crear(AporteRequest solicitud) {
        validarReglasPorTipo(solicitud);

        Aporte nuevo = new Aporte();
        aplicar(nuevo, solicitud);
        nuevo.setEstado(EstadoContacto.PENDIENTE);
        nuevo.setFechaRegistro(reloj.millis());
        return aRespuesta(repositorio.save(nuevo));
    }

    @Transactional(readOnly = true)
    public List<AporteResponse> listar(TipoRegistro tipo) {
        List<Aporte> lista = (tipo == null)
                ? repositorio.findAllByOrderByFechaRegistroDesc()
                : repositorio.findByTipoOrderByFechaRegistroDesc(tipo);
        return lista.stream().map(this::aRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public AporteResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public AporteResponse actualizar(Long id, AporteRequest solicitud) {
        validarReglasPorTipo(solicitud);

        Aporte existente = buscar(id);
        aplicar(existente, solicitud);
        return aRespuesta(repositorio.save(existente));
    }

    @Transactional
    public AporteResponse cambiarEstado(Long id, EstadoContacto estado) {
        Aporte existente = buscar(id);
        existente.setEstado(estado);
        return aRespuesta(repositorio.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw ApiException.noEncontrado("No existe el aporte con id " + id);
        }
        repositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ResumenResponse resumen() {
        return new ResumenResponse(
                repositorio.count(),
                repositorio.countByTipo(TipoRegistro.PERSONA),
                repositorio.countByTipo(TipoRegistro.EMPRESA),
                repositorio.countByEstado(EstadoContacto.PENDIENTE));
    }

    private Aporte buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado("No existe el aporte con id " + id));
    }

    // Reglas que dependen del tipo de formulario (las mismas que valida la app)
    private void validarReglasPorTipo(AporteRequest s) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (s.tipo() == TipoRegistro.PERSONA) {
            if (s.tipoAporte() == null || !FRECUENCIAS.contains(s.tipoAporte().trim())) {
                errores.put("tipoAporte", "Selecciona la frecuencia: Donación única, Mensual o Anual");
            }
        } else if (s.tipo() == TipoRegistro.EMPRESA) {
            exigir(errores, "direccion", s.direccion());
            exigir(errores, "giro", s.giro());
            exigir(errores, "representante", s.representante());
            exigir(errores, "rutRepresentante", s.rutRepresentante());
        }

        if (!errores.isEmpty()) {
            throw ApiException.solicitudInvalida("Datos inválidos", errores);
        }
    }

    private void exigir(Map<String, String> errores, String campo, String valor) {
        if (valor == null || valor.isBlank()) {
            errores.put(campo, "Campo obligatorio para empresas");
        }
    }

    private void aplicar(Aporte destino, AporteRequest s) {
        destino.setTipo(s.tipo());
        destino.setOportunidadId(s.oportunidadId());
        destino.setOportunidad(s.oportunidad().trim());
        destino.setNombre(s.nombre().trim());
        destino.setRut(s.rut().trim().toUpperCase());
        destino.setCorreo(s.correo().trim());
        destino.setTelefono(s.telefono().trim());

        if (s.tipo() == TipoRegistro.EMPRESA) {
            destino.setTipoAporte(APORTE_EMPRESARIAL);
            destino.setDireccion(s.direccion().trim());
            destino.setGiro(s.giro().trim());
            destino.setRepresentante(s.representante().trim());
            destino.setRutRepresentante(s.rutRepresentante().trim().toUpperCase());
        } else {
            destino.setTipoAporte(s.tipoAporte().trim());
            destino.setDireccion(null);
            destino.setGiro(null);
            destino.setRepresentante(null);
            destino.setRutRepresentante(null);
        }
    }

    private String texto(String valor) {
        return valor == null ? "" : valor;
    }

    private AporteResponse aRespuesta(Aporte a) {
        return new AporteResponse(
                a.getId(),
                a.getTipo().name(),
                a.getOportunidadId(),
                a.getOportunidad(),
                a.getNombre(),
                a.getRut(),
                a.getCorreo(),
                a.getTelefono(),
                a.getTipoAporte(),
                texto(a.getDireccion()),
                texto(a.getGiro()),
                texto(a.getRepresentante()),
                texto(a.getRutRepresentante()),
                a.getEstado().name(),
                a.getFechaRegistro());
    }
}
