package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.HistorialEstadoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.HistorialEstadoPago;
import org.springframework.stereotype.Component;

@Component
public class HistorialEstadoPagoMapper {

    public HistorialEstadoPagoResponse toResponse(HistorialEstadoPago historial) {
        return HistorialEstadoPagoResponse.builder()
                .id(historial.getId())
                .idPago(historial.getIdPago())
                .estadoAnterior(historial.getEstadoAnterior())
                .estadoNuevo(historial.getEstadoNuevo())
                .fecha(historial.getFecha())
                .build();
    }
}
