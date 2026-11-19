package cl.corpaliv.aportes.repository;

import cl.corpaliv.aportes.model.Aporte;
import cl.corpaliv.aportes.model.EstadoContacto;
import cl.corpaliv.aportes.model.MotivoRegistro;
import cl.corpaliv.aportes.model.TipoRegistro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AporteRepository extends JpaRepository<Aporte, Long> {

    List<Aporte> findAllByOrderByFechaRegistroDesc();

    List<Aporte> findByTipoOrderByFechaRegistroDesc(TipoRegistro tipo);

    List<Aporte> findByMotivoOrderByFechaRegistroDesc(MotivoRegistro motivo);

    List<Aporte> findByTipoAndMotivoOrderByFechaRegistroDesc(TipoRegistro tipo, MotivoRegistro motivo);

    long countByMotivo(MotivoRegistro motivo);

    long countByTipoAndMotivo(TipoRegistro tipo, MotivoRegistro motivo);

    long countByEstado(EstadoContacto estado);
}
