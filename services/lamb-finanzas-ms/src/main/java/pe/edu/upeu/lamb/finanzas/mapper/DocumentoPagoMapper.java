package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.DocumentoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.DocumentoPago;
import org.springframework.stereotype.Component;

@Component
public class DocumentoPagoMapper {

    public DocumentoPagoResponse toResponse(DocumentoPago documento) {
        return DocumentoPagoResponse.builder()
                .id(documento.getId())
                .idPago(documento.getIdPago())
                .tipo(documento.getTipo())
                .numero(documento.getNumero())
                .estado(documento.getEstado())
                .fechaEmision(documento.getFechaEmision())
                .build();
    }
}
