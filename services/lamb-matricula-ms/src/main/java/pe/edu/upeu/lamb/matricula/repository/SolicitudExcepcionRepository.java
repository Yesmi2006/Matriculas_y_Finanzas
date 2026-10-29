package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.EstadoSolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.entity.SolicitudExcepcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudExcepcionRepository extends JpaRepository<SolicitudExcepcion, Long> {
    List<SolicitudExcepcion> findByIdPersona(Long idPersona);

    List<SolicitudExcepcion> findByEstado(EstadoSolicitudExcepcion estado);
}
