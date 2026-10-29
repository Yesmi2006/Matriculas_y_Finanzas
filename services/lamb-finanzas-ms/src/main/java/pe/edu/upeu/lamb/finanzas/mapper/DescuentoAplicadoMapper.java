package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.DescuentoAplicadoRequest;
import pe.edu.upeu.lamb.finanzas.dto.DescuentoAplicadoResponse;
import pe.edu.upeu.lamb.finanzas.entity.DescuentoAplicado;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DescuentoAplicadoMapper {

    public DescuentoAplicado toEntity(DescuentoAplicadoRequest request) {
        return DescuentoAplicado.builder()
                .idPersona(request.getIdPersona())
                .idPago(request.getIdPago())
                .tipo(request.getTipo())
                .valor(request.getValor())
                .descripcion(request.getDescripcion())
                .fechaAplicacion(Instant.now())
                .build();
    }

    public void actualizar(DescuentoAplicado descuento, DescuentoAplicadoRequest request) {
        descuento.setIdPersona(request.getIdPersona());
        descuento.setIdPago(request.getIdPago());
        descuento.setTipo(request.getTipo());
        descuento.setValor(request.getValor());
        descuento.setDescripcion(request.getDescripcion());
    }

    public DescuentoAplicadoResponse toResponse(DescuentoAplicado descuento) {
        return DescuentoAplicadoResponse.builder()
                .id(descuento.getId())
                .idPersona(descuento.getIdPersona())
                .idPago(descuento.getIdPago())
                .tipo(descuento.getTipo())
                .valor(descuento.getValor())
                .descripcion(descuento.getDescripcion())
                .fechaAplicacion(descuento.getFechaAplicacion())
                .build();
    }
}
