package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.EstadoReserva;
import pe.edu.upeu.lamb.matricula.entity.ReservaVacante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaVacanteRepository extends JpaRepository<ReservaVacante, Long> {
    List<ReservaVacante> findByIdPersona(Long idPersona);

    List<ReservaVacante> findByIdSeccionAndEstado(Long idSeccion, EstadoReserva estado);
}
