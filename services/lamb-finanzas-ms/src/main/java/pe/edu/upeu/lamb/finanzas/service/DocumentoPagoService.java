package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.DocumentoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.DocumentoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.DocumentoPago;
import pe.edu.upeu.lamb.finanzas.entity.EstadoDocumento;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.DocumentoPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.DocumentoPagoRepository;
import pe.edu.upeu.lamb.finanzas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoPagoService {

    private final DocumentoPagoRepository documentoPagoRepository;
    private final DocumentoPagoMapper documentoPagoMapper;
    private final PagoRepository pagoRepository;

    public List<DocumentoPagoResponse> listar(Long idPago) {
        List<DocumentoPago> documentos = idPago != null
                ? documentoPagoRepository.findByIdPago(idPago)
                : documentoPagoRepository.findAll();
        return documentos.stream().map(documentoPagoMapper::toResponse).toList();
    }

    public DocumentoPagoResponse obtener(Long id) {
        return documentoPagoMapper.toResponse(buscarOFallar(id));
    }

    public DocumentoPagoResponse crear(DocumentoPagoRequest request) {
        if (!pagoRepository.existsById(request.getIdPago())) {
            throw new ResourceNotFoundException("Pago no encontrado: " + request.getIdPago());
        }
        DocumentoPago documento = DocumentoPago.builder()
                .idPago(request.getIdPago())
                .tipo(request.getTipo())
                .numero(request.getTipo() + "-" + UUID.randomUUID())
                .estado(EstadoDocumento.EMITIDO)
                .fechaEmision(Instant.now())
                .build();
        return documentoPagoMapper.toResponse(documentoPagoRepository.save(documento));
    }

    public DocumentoPagoResponse anular(Long id) {
        DocumentoPago documento = buscarOFallar(id);
        if (documento.getEstado() == EstadoDocumento.ANULADO) {
            return documentoPagoMapper.toResponse(documento);
        }
        documento.setEstado(EstadoDocumento.ANULADO);
        return documentoPagoMapper.toResponse(documentoPagoRepository.save(documento));
    }

    private DocumentoPago buscarOFallar(Long id) {
        return documentoPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de pago no encontrado: " + id));
    }
}
