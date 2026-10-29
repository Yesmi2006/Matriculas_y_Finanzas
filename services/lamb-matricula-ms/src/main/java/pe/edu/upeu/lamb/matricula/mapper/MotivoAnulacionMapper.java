package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.MotivoAnulacionRequest;
import pe.edu.upeu.lamb.matricula.dto.MotivoAnulacionResponse;
import pe.edu.upeu.lamb.matricula.entity.MotivoAnulacion;
import org.springframework.stereotype.Component;

@Component
public class MotivoAnulacionMapper {

    public MotivoAnulacion toEntity(MotivoAnulacionRequest request) {
        return MotivoAnulacion.builder()
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .vigente(request.getVigente() == null || request.getVigente())
                .build();
    }

    public void actualizar(MotivoAnulacion motivo, MotivoAnulacionRequest request) {
        motivo.setCodigo(request.getCodigo());
        motivo.setDescripcion(request.getDescripcion());
        if (request.getVigente() != null) {
            motivo.setVigente(request.getVigente());
        }
    }

    public MotivoAnulacionResponse toResponse(MotivoAnulacion motivo) {
        return MotivoAnulacionResponse.builder()
                .id(motivo.getId())
                .codigo(motivo.getCodigo())
                .descripcion(motivo.getDescripcion())
                .vigente(motivo.isVigente())
                .build();
    }
}
