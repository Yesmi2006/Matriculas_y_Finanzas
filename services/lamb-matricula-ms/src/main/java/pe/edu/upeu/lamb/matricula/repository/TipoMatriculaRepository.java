package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.TipoMatricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoMatriculaRepository extends JpaRepository<TipoMatricula, Long> {
    Optional<TipoMatricula> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
