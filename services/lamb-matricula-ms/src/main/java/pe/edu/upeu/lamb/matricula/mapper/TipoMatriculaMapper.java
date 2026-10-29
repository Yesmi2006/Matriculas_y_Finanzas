package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.TipoMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.TipoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.TipoMatricula;
import org.springframework.stereotype.Component;

@Component
public class TipoMatriculaMapper {

    public TipoMatricula toEntity(TipoMatriculaRequest request) {
        return TipoMatricula.builder()
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .diasPlazoExtra(request.getDiasPlazoExtra())
                .recargo(request.getRecargo())
                .vigente(request.getVigente() == null || request.getVigente())
                .build();
    }

    public void actualizar(TipoMatricula tipo, TipoMatriculaRequest request) {
        tipo.setCodigo(request.getCodigo());
        tipo.setDescripcion(request.getDescripcion());
        tipo.setDiasPlazoExtra(request.getDiasPlazoExtra());
        tipo.setRecargo(request.getRecargo());
        if (request.getVigente() != null) {
            tipo.setVigente(request.getVigente());
        }
    }

    public TipoMatriculaResponse toResponse(TipoMatricula tipo) {
        return TipoMatriculaResponse.builder()
                .id(tipo.getId())
                .codigo(tipo.getCodigo())
                .descripcion(tipo.getDescripcion())
                .diasPlazoExtra(tipo.getDiasPlazoExtra())
                .recargo(tipo.getRecargo())
                .vigente(tipo.isVigente())
                .build();
    }
}
