package cl.corpaliv.aportes.repository;

import cl.corpaliv.aportes.model.Aporte;
import cl.corpaliv.aportes.model.EstadoContacto;
import cl.corpaliv.aportes.model.TipoRegistro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AporteRepository extends JpaRepository<Aporte, Long> {

    List<Aporte> findAllByOrderByFechaRegistroDesc();

    List<Aporte> findByTipoOrderByFechaRegistroDesc(TipoRegistro tipo);

    long countByTipo(TipoRegistro tipo);

    long countByEstado(EstadoContacto estado);
}
