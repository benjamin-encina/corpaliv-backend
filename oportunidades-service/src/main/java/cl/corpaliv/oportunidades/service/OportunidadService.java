package cl.corpaliv.oportunidades.service;

import cl.corpaliv.oportunidades.dto.OportunidadRequest;
import cl.corpaliv.oportunidades.dto.OportunidadResponse;
import cl.corpaliv.oportunidades.exception.ApiException;
import cl.corpaliv.oportunidades.model.Oportunidad;
import cl.corpaliv.oportunidades.repository.OportunidadRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OportunidadService {

    private final OportunidadRepository repositorio;

    public OportunidadService(OportunidadRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<OportunidadResponse> listar(String categoria) {
        List<Oportunidad> lista = (categoria == null || categoria.isBlank())
                ? repositorio.findAllByOrderByIdAsc()
                : repositorio.findByCategoriaIgnoreCaseOrderByIdAsc(categoria.trim());
        return lista.stream().map(this::aRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public OportunidadResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public OportunidadResponse crear(OportunidadRequest solicitud) {
        Oportunidad nueva = new Oportunidad();
        aplicar(nueva, solicitud);
        return aRespuesta(repositorio.save(nueva));
    }

    @Transactional
    public OportunidadResponse actualizar(Long id, OportunidadRequest solicitud) {
        Oportunidad existente = buscar(id);
        aplicar(existente, solicitud);
        return aRespuesta(repositorio.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw ApiException.noEncontrado("No existe la oportunidad con id " + id);
        }
        repositorio.deleteById(id);
    }

    private Oportunidad buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado("No existe la oportunidad con id " + id));
    }

    private void aplicar(Oportunidad destino, OportunidadRequest s) {
        destino.setTitulo(s.titulo().trim());
        destino.setCategoria(s.categoria().trim());
        destino.setDescripcion(s.descripcion().trim());
        destino.setImpactoSocial(s.impactoSocial().trim());
        destino.setImagenUrl(opcional(s.imagenUrl()));
        destino.setContactoEmail(opcional(s.contactoEmail()));
        destino.setMetaFinanciamiento(s.metaFinanciamiento());
        destino.setMontoRecaudado(s.montoRecaudado());
    }

    // Un texto vacío se guarda como null para que la app use su valor por defecto (null safety)
    private String opcional(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

    private OportunidadResponse aRespuesta(Oportunidad o) {
        return new OportunidadResponse(o.getId(), o.getTitulo(), o.getCategoria(), o.getDescripcion(),
                o.getImpactoSocial(), o.getImagenUrl(), o.getContactoEmail(),
                o.getMetaFinanciamiento(), o.getMontoRecaudado());
    }
}
