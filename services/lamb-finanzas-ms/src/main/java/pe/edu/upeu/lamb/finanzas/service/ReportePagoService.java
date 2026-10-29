package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.ReporteCuotasVencidasResponse;
import pe.edu.upeu.lamb.finanzas.dto.ReportePorMetodoResponse;
import pe.edu.upeu.lamb.finanzas.entity.CuotaPago;
import pe.edu.upeu.lamb.finanzas.entity.EstadoCuota;
import pe.edu.upeu.lamb.finanzas.entity.EstadoPago;
import pe.edu.upeu.lamb.finanzas.entity.MetodoPago;
import pe.edu.upeu.lamb.finanzas.entity.Pago;
import pe.edu.upeu.lamb.finanzas.mapper.CuotaPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.CuotaPagoRepository;
import pe.edu.upeu.lamb.finanzas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportePagoService {

    private final PagoRepository pagoRepository;
    private final CuotaPagoRepository cuotaPagoRepository;
    private final CuotaPagoMapper cuotaPagoMapper;

    public List<ReportePorMetodoResponse> porMetodo() {
        List<Pago> pagos = pagoRepository.findAll().stream()
                .filter(p -> p.getEstado() == EstadoPago.APROBADO)
                .toList();
        return Arrays.stream(MetodoPago.values())
                .map(metodo -> {
                    List<Pago> delMetodo = pagos.stream().filter(p -> p.getMetodo() == metodo).toList();
                    BigDecimal montoTotal = delMetodo.stream()
                            .map(Pago::getMonto)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return ReportePorMetodoResponse.builder()
                            .metodo(metodo)
                            .cantidad(delMetodo.size())
                            .montoTotal(montoTotal)
                            .build();
                })
                .toList();
    }

    @Transactional
    public ReporteCuotasVencidasResponse cuotasVencidas() {
        Instant ahora = Instant.now();
        List<CuotaPago> vencidas = cuotaPagoRepository.findAll().stream()
                .filter(c -> c.getEstado() == EstadoCuota.PENDIENTE && c.getFechaVencimiento().isBefore(ahora))
                .toList();
        vencidas.forEach(c -> c.setEstado(EstadoCuota.VENCIDA));
        cuotaPagoRepository.saveAll(vencidas);
        BigDecimal montoTotal = vencidas.stream()
                .map(CuotaPago::getMontoCuota)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<CuotaPagoResponse> cuotas = vencidas.stream().map(cuotaPagoMapper::toResponse).toList();
        return ReporteCuotasVencidasResponse.builder()
                .cantidad(cuotas.size())
                .montoTotal(montoTotal)
                .cuotas(cuotas)
                .build();
    }
}
