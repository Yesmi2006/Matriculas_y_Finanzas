package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.ReservaVacanteResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoReserva;
import pe.edu.upeu.lamb.matricula.entity.ReservaVacante;
import pe.edu.upeu.lamb.matricula.mapper.ReservaVacanteMapper;
import pe.edu.upeu.lamb.matricula.repository.ReservaVacanteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaVacanteServiceTest {

    @Mock
    private ReservaVacanteRepository reservaVacanteRepository;
    @Mock
    private ReservaVacanteMapper reservaVacanteMapper;
    @Mock
    private SeccionService seccionService;

    @InjectMocks
    private ReservaVacanteService reservaVacanteService;

    private ReservaVacante reserva(Long id, Long idPersona, Long idSeccion, EstadoReserva estado) {
        Instant ahora = Instant.now();
        return ReservaVacante.builder()
                .id(id)
                .idPersona(idPersona)
                .idSeccion(idSeccion)
                .fechaReserva(ahora)
                .fechaExpiracion(ahora.plusSeconds(600))
                .estado(estado)
                .build();
    }

    private void stubMapper() {
        when(reservaVacanteMapper.toResponse(any(ReservaVacante.class)))
                .thenAnswer(inv -> {
                    ReservaVacante r = (ReservaVacante) inv.getArgument(0);
                    return ReservaVacanteResponse.builder()
                            .id(r.getId())
                            .idPersona(r.getIdPersona())
                            .idSeccion(r.getIdSeccion())
                            .estado(r.getEstado())
                            .build();
                });
    }

    @Test
    void listar_sinFiltros_devuelveTodasLasReservas() {
        when(reservaVacanteRepository.findAll()).thenReturn(List.of(
                reserva(1L, 100L, 5L, EstadoReserva.ACTIVA),
                reserva(2L, 200L, 6L, EstadoReserva.CANCELADA)));
        stubMapper();

        List<ReservaVacanteResponse> resultado = reservaVacanteService.listar(null, null, null);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void listar_combinaIdPersonaIdSeccionYEstado() {
        when(reservaVacanteRepository.findByIdPersona(100L)).thenReturn(List.of(
                reserva(1L, 100L, 5L, EstadoReserva.ACTIVA),
                reserva(2L, 100L, 6L, EstadoReserva.ACTIVA),
                reserva(3L, 100L, 5L, EstadoReserva.CANCELADA)));
        stubMapper();

        List<ReservaVacanteResponse> resultado = reservaVacanteService.listar(100L, 5L, EstadoReserva.ACTIVA);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
    }
}
