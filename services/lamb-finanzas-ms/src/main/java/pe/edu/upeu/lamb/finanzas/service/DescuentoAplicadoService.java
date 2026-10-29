package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.DescuentoAplicadoRequest;
import pe.edu.upeu.lamb.finanzas.dto.DescuentoAplicadoResponse;
import pe.edu.upeu.lamb.finanzas.entity.DescuentoAplicado;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.DescuentoAplicadoMapper;
import pe.edu.upeu.lamb.finanzas.repository.DescuentoAplicadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DescuentoAplicadoService {

    private final DescuentoAplicadoRepository descuentoAplicadoRepository;
    private final DescuentoAplicadoMapper descuentoAplicadoMapper;

    public List<DescuentoAplicadoResponse> listar(Long idPersona, Long idPago) {
        List<DescuentoAplicado> descuentos;
        if (idPago != null) {
            descuentos = descuentoAplicadoRepository.findByIdPago(idPago);
        } else if (idPersona != null) {
            descuentos = descuentoAplicadoRepository.findByIdPersona(idPersona);
        } else {
            descuentos = descuentoAplicadoRepository.findAll();
        }
        return descuentos.stream().map(descuentoAplicadoMapper::toResponse).toList();
    }

    public DescuentoAplicadoResponse obtener(Long id) {
        return descuentoAplicadoMapper.toResponse(buscarOFallar(id));
    }

    public DescuentoAplicadoResponse crear(DescuentoAplicadoRequest request) {
        return descuentoAplicadoMapper.toResponse(
                descuentoAplicadoRepository.save(descuentoAplicadoMapper.toEntity(request)));
    }

    public DescuentoAplicadoResponse actualizar(Long id, DescuentoAplicadoRequest request) {
        DescuentoAplicado descuento = buscarOFallar(id);
        descuentoAplicadoMapper.actualizar(descuento, request);
        return descuentoAplicadoMapper.toResponse(descuentoAplicadoRepository.save(descuento));
    }

    public void eliminar(Long id) {
        descuentoAplicadoRepository.delete(buscarOFallar(id));
    }

    private DescuentoAplicado buscarOFallar(Long id) {
        return descuentoAplicadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado: " + id));
    }
}
