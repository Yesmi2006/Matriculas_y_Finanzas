package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.TransaccionPasarelaResponse;
import pe.edu.upeu.lamb.finanzas.entity.TransaccionPasarela;
import org.springframework.stereotype.Component;

@Component
public class TransaccionPasarelaMapper {

    public TransaccionPasarelaResponse toResponse(TransaccionPasarela transaccion) {
        return TransaccionPasarelaResponse.builder()
                .id(transaccion.getId())
                .idPago(transaccion.getIdPago())
                .referencia(transaccion.getReferencia())
                .monto(transaccion.getMonto())
                .estado(transaccion.getEstado())
                .fecha(transaccion.getFecha())
                .build();
    }
}
