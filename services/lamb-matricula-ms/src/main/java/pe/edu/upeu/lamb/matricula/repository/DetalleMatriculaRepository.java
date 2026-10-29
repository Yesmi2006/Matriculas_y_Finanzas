package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.DetalleMatricula;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula, Long> {

    @Query("select count(d) from DetalleMatricula d where d.idSeccion = :idSeccion and d.matricula.estado in :estados")
    long contarMatriculadosEnSeccion(@Param("idSeccion") Long idSeccion, @Param("estados") Iterable<EstadoMatricula> estados);
}
