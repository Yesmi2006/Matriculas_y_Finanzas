package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.TransaccionPasarelaResponse;
import pe.edu.upeu.lamb.finanzas.entity.EstadoTransaccionPasarela;
import pe.edu.upeu.lamb.finanzas.entity.TransaccionPasarela;
import pe.edu.upeu.lamb.finanzas.mapper.TransaccionPasarelaMapper;
import pe.edu.upeu.lamb.finanzas.repository.TransaccionPasarelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Solo lectura desde la API (no se expone POST/PUT/DELETE): es un registro de auditoria que
 * PagoService alimenta automaticamente al aprobar o procesar un pago.
 */
@Service
@RequiredArgsConstructor
public class TransaccionPasarelaService {

    private final TransaccionPasarelaRepository transaccionPasarelaRepository;
    private final TransaccionPasarelaMapper transaccionPasarelaMapper;

    public List<TransaccionPasarelaResponse> listarPorPago(Long idPago) {
        return transaccionPasarelaRepository.findByIdPagoOrderByFechaDesc(idPago).stream()
                .map(transaccionPasarelaMapper::toResponse)
                .toList();
    }

    public void registrar(Long idPago, BigDecimal monto, EstadoTransaccionPasarela estado) {
        transaccionPasarelaRepository.save(TransaccionPasarela.builder()
                .idPago(idPago)
                .referencia(UUID.randomUUID().toString())
                .monto(monto)
                .estado(estado)
                .fecha(Instant.now())
                .build());
    }
}
