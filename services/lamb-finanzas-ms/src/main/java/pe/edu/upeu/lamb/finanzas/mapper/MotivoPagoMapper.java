package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.MotivoPago;
import org.springframework.stereotype.Component;

@Component
public class MotivoPagoMapper {

    public MotivoPago toEntity(MotivoPagoRequest request) {
        return MotivoPago.builder()
                .tipo(request.getTipo())
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .vigente(request.getVigente() == null || request.getVigente())
                .build();
    }

    public void actualizar(MotivoPago motivo, MotivoPagoRequest request) {
        motivo.setTipo(request.getTipo());
        motivo.setCodigo(request.getCodigo());
        motivo.setDescripcion(request.getDescripcion());
        if (request.getVigente() != null) {
            motivo.setVigente(request.getVigente());
        }
    }

    public MotivoPagoResponse toResponse(MotivoPago motivo) {
        return MotivoPagoResponse.builder()
                .id(motivo.getId())
                .tipo(motivo.getTipo())
                .codigo(motivo.getCodigo())
                .descripcion(motivo.getDescripcion())
                .vigente(motivo.isVigente())
                .build();
    }
}
