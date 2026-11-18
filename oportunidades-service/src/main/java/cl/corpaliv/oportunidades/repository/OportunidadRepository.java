package cl.corpaliv.oportunidades.repository;

import cl.corpaliv.oportunidades.model.Oportunidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OportunidadRepository extends JpaRepository<Oportunidad, Long> {

    List<Oportunidad> findAllByOrderByIdAsc();

    List<Oportunidad> findByCategoriaIgnoreCaseOrderByIdAsc(String categoria);
}
