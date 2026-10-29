package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.HistorialEstadoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.EstadoPago;
import pe.edu.upeu.lamb.finanzas.entity.HistorialEstadoPago;
import pe.edu.upeu.lamb.finanzas.mapper.HistorialEstadoPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.HistorialEstadoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Solo lectura desde la API (no se expone POST/PUT/DELETE): es un registro de auditoria que
 * PagoService alimenta automaticamente en cada transicion de estado.
 */
@Service
@RequiredArgsConstructor
public class HistorialEstadoPagoService {

    private final HistorialEstadoPagoRepository historialEstadoPagoRepository;
    private final HistorialEstadoPagoMapper historialEstadoPagoMapper;

    public List<HistorialEstadoPagoResponse> listarPorPago(Long idPago) {
        return historialEstadoPagoRepository.findByIdPagoOrderByFechaAsc(idPago).stream()
                .map(historialEstadoPagoMapper::toResponse)
                .toList();
    }

    public void registrar(Long idPago, EstadoPago estadoAnterior, EstadoPago estadoNuevo) {
        historialEstadoPagoRepository.save(HistorialEstadoPago.builder()
                .idPago(idPago)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .fecha(Instant.now())
                .build());
    }
}
