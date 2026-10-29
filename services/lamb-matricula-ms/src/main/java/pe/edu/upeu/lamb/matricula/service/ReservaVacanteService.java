package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.ReservaVacanteRequest;
import pe.edu.upeu.lamb.matricula.dto.ReservaVacanteResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoReserva;
import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import pe.edu.upeu.lamb.matricula.entity.ReservaVacante;
import pe.edu.upeu.lamb.matricula.entity.Seccion;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.ReservaVacanteMapper;
import pe.edu.upeu.lamb.matricula.repository.ReservaVacanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaVacanteService {

    private final ReservaVacanteRepository reservaVacanteRepository;
    private final ReservaVacanteMapper reservaVacanteMapper;
    private final SeccionService seccionService;

    public List<ReservaVacanteResponse> listar(Long idPersona, Long idSeccion, EstadoReserva estado) {
        List<ReservaVacante> reservas = idPersona != null
                ? reservaVacanteRepository.findByIdPersona(idPersona)
                : reservaVacanteRepository.findAll();
        if (idSeccion != null) {
            reservas = reservas.stream().filter(r -> r.getIdSeccion().equals(idSeccion)).toList();
        }
        if (estado != null) {
            reservas = reservas.stream().filter(r -> r.getEstado() == estado).toList();
        }
        return reservas.stream().map(reservaVacanteMapper::toResponse).toList();
    }

    public ReservaVacanteResponse obtener(Long id) {
        return reservaVacanteMapper.toResponse(buscarOFallar(id));
    }

    /**
     * Cupo disponible = cupo maximo - matriculados ya registrados - otras reservas ACTIVAS
     * vigentes (no vencidas) en la misma seccion.
     */
    public ReservaVacanteResponse crear(ReservaVacanteRequest request) {
        Seccion seccion = seccionService.buscarOFallar(request.getIdSeccion());
        if (seccion.getEstado() != EstadoSeccion.ABIERTA) {
            throw new BusinessRuleException("La seccion " + seccion.getId() + " no esta ABIERTA");
        }
        long ocupado = seccionService.cupoOcupado(seccion.getId()) + contarReservasActivasVigentes(seccion.getId());
        if (ocupado >= seccion.getCupoMaximo()) {
            throw new BusinessRuleException("No hay cupo disponible para reservar en la seccion " + seccion.getId());
        }
        Instant ahora = Instant.now();
        ReservaVacante reserva = ReservaVacante.builder()
                .idPersona(request.getIdPersona())
                .idSeccion(request.getIdSeccion())
                .fechaReserva(ahora)
                .fechaExpiracion(ahora.plus(request.getMinutosVigencia(), ChronoUnit.MINUTES))
                .estado(EstadoReserva.ACTIVA)
                .build();
        return reservaVacanteMapper.toResponse(reservaVacanteRepository.save(reserva));
    }

    public ReservaVacanteResponse cancelar(Long id) {
        ReservaVacante reserva = buscarOFallar(id);
        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new BusinessRuleException("Solo se puede cancelar una reserva ACTIVA (id=" + id
                    + ", estado actual=" + reserva.getEstado() + ")");
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaVacanteMapper.toResponse(reservaVacanteRepository.save(reserva));
    }

    public void eliminar(Long id) {
        reservaVacanteRepository.delete(buscarOFallar(id));
    }

    private long contarReservasActivasVigentes(Long idSeccion) {
        Instant ahora = Instant.now();
        return reservaVacanteRepository.findByIdSeccionAndEstado(idSeccion, EstadoReserva.ACTIVA).stream()
                .filter(r -> r.getFechaExpiracion().isAfter(ahora))
                .count();
    }

    private ReservaVacante buscarOFallar(Long id) {
        return reservaVacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva de vacante no encontrada: " + id));
    }
}
