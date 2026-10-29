package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.CuotaPago;
import pe.edu.upeu.lamb.finanzas.entity.EstadoCuota;
import org.springframework.stereotype.Component;

@Component
public class CuotaPagoMapper {

    public CuotaPago toEntity(Long idPago, CuotaPagoRequest request) {
        return CuotaPago.builder()
                .idPago(idPago)
                .numeroCuota(request.getNumeroCuota())
                .montoCuota(request.getMontoCuota())
                .fechaVencimiento(request.getFechaVencimiento())
                .estado(EstadoCuota.PENDIENTE)
                .build();
    }

    public void actualizar(CuotaPago cuota, CuotaPagoRequest request) {
        cuota.setNumeroCuota(request.getNumeroCuota());
        cuota.setMontoCuota(request.getMontoCuota());
        cuota.setFechaVencimiento(request.getFechaVencimiento());
    }

    public CuotaPagoResponse toResponse(CuotaPago cuota) {
        return CuotaPagoResponse.builder()
                .id(cuota.getId())
                .idPago(cuota.getIdPago())
                .numeroCuota(cuota.getNumeroCuota())
                .montoCuota(cuota.getMontoCuota())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .estado(cuota.getEstado())
                .fechaPago(cuota.getFechaPago())
                .build();
    }
}
