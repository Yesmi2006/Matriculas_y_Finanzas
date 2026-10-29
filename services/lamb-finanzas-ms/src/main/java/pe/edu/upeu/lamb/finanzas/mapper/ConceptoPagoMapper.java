package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.ConceptoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ConceptoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.ConceptoPago;
import org.springframework.stereotype.Component;

@Component
public class ConceptoPagoMapper {

    public ConceptoPago toEntity(ConceptoPagoRequest request) {
        return ConceptoPago.builder()
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .monto(request.getMonto())
                .vigente(request.getVigente() == null || request.getVigente())
                .build();
    }

    public void actualizar(ConceptoPago concepto, ConceptoPagoRequest request) {
        concepto.setCodigo(request.getCodigo());
        concepto.setDescripcion(request.getDescripcion());
        concepto.setMonto(request.getMonto());
        if (request.getVigente() != null) {
            concepto.setVigente(request.getVigente());
        }
    }

    public ConceptoPagoResponse toResponse(ConceptoPago concepto) {
        return ConceptoPagoResponse.builder()
                .id(concepto.getId())
                .codigo(concepto.getCodigo())
                .descripcion(concepto.getDescripcion())
                .monto(concepto.getMonto())
                .vigente(concepto.isVigente())
                .build();
    }
}
