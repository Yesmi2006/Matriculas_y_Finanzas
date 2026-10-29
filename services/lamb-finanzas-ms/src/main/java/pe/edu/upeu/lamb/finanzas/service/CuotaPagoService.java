package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.CuotaPago;
import pe.edu.upeu.lamb.finanzas.entity.EstadoCuota;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.CuotaPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.CuotaPagoRepository;
import pe.edu.upeu.lamb.finanzas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuotaPagoService {

    private final CuotaPagoRepository cuotaPagoRepository;
    private final CuotaPagoMapper cuotaPagoMapper;
    private final PagoRepository pagoRepository;

    public List<CuotaPagoResponse> listarPorPago(Long idPago) {
        validarPagoExiste(idPago);
        return cuotaPagoRepository.findByIdPagoOrderByNumeroCuotaAsc(idPago).stream()
                .map(cuotaPagoMapper::toResponse)
                .toList();
    }

    public CuotaPagoResponse obtener(Long idPago, Long cuotaId) {
        return cuotaPagoMapper.toResponse(buscarOFallar(idPago, cuotaId));
    }

    public CuotaPagoResponse crear(Long idPago, CuotaPagoRequest request) {
        validarPagoExiste(idPago);
        if (cuotaPagoRepository.existsByIdPagoAndNumeroCuota(idPago, request.getNumeroCuota())) {
            throw new BusinessRuleException("El pago " + idPago + " ya tiene una cuota numero "
                    + request.getNumeroCuota());
        }
        return cuotaPagoMapper.toResponse(
                cuotaPagoRepository.save(cuotaPagoMapper.toEntity(idPago, request)));
    }

    public CuotaPagoResponse actualizar(Long idPago, Long cuotaId, CuotaPagoRequest request) {
        CuotaPago cuota = buscarOFallar(idPago, cuotaId);
        if (cuota.getEstado() != EstadoCuota.PENDIENTE) {
            throw new BusinessRuleException("Solo se puede editar una cuota PENDIENTE (id=" + cuotaId
                    + ", estado actual=" + cuota.getEstado() + ")");
        }
        cuotaPagoMapper.actualizar(cuota, request);
        return cuotaPagoMapper.toResponse(cuotaPagoRepository.save(cuota));
    }

    public void eliminar(Long idPago, Long cuotaId) {
        cuotaPagoRepository.delete(buscarOFallar(idPago, cuotaId));
    }

    public CuotaPagoResponse pagar(Long idPago, Long cuotaId) {
        CuotaPago cuota = buscarOFallar(idPago, cuotaId);
        if (cuota.getEstado() == EstadoCuota.PAGADA) {
            return cuotaPagoMapper.toResponse(cuota);
        }
        cuota.setEstado(EstadoCuota.PAGADA);
        cuota.setFechaPago(Instant.now());
        return cuotaPagoMapper.toResponse(cuotaPagoRepository.save(cuota));
    }

    private void validarPagoExiste(Long idPago) {
        if (!pagoRepository.existsById(idPago)) {
            throw new ResourceNotFoundException("Pago no encontrado: " + idPago);
        }
    }

    private CuotaPago buscarOFallar(Long idPago, Long cuotaId) {
        CuotaPago cuota = cuotaPagoRepository.findById(cuotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota no encontrada: " + cuotaId));
        if (!cuota.getIdPago().equals(idPago)) {
            throw new ResourceNotFoundException("La cuota " + cuotaId + " no pertenece al pago " + idPago);
        }
        return cuota;
    }
}
