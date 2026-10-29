package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long>, JpaSpecificationExecutor<Matricula> {
    List<Matricula> findByIdPersona(Long idPersona);
}
