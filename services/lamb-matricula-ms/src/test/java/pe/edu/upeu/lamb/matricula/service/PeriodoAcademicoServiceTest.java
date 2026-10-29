package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoPeriodo;
import pe.edu.upeu.lamb.matricula.entity.PeriodoAcademico;
import pe.edu.upeu.lamb.matricula.mapper.PeriodoAcademicoMapper;
import pe.edu.upeu.lamb.matricula.repository.PeriodoAcademicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeriodoAcademicoServiceTest {

    @Mock
    private PeriodoAcademicoRepository periodoAcademicoRepository;
    @Mock
    private PeriodoAcademicoMapper periodoAcademicoMapper;

    @InjectMocks
    private PeriodoAcademicoService periodoAcademicoService;

    private PeriodoAcademico periodo(Long id, EstadoPeriodo estado) {
        return PeriodoAcademico.builder()
                .id(id)
                .codigo("2026-" + id)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(4))
                .fechaLimiteMatricula(LocalDate.now().plusWeeks(1))
                .estado(estado)
                .build();
    }

    @Test
    void listar_sinFiltro_devuelveTodos() {
        when(periodoAcademicoRepository.findAll()).thenReturn(
                List.of(periodo(1L, EstadoPeriodo.ABIERTO), periodo(2L, EstadoPeriodo.CERRADO)));
        when(periodoAcademicoMapper.toResponse(any(PeriodoAcademico.class)))
                .thenAnswer(inv -> PeriodoAcademicoResponse.builder()
                        .id(((PeriodoAcademico) inv.getArgument(0)).getId())
                        .estado(((PeriodoAcademico) inv.getArgument(0)).getEstado())
                        .build());

        List<PeriodoAcademicoResponse> resultado = periodoAcademicoService.listar(null);

        assertThat(resultado).hasSize(2);
        verify(periodoAcademicoRepository, never()).findByEstado(any());
    }

    @Test
    void listar_conFiltroEstado_usaElRepositorioFiltrado() {
        when(periodoAcademicoRepository.findByEstado(EstadoPeriodo.ABIERTO))
                .thenReturn(List.of(periodo(1L, EstadoPeriodo.ABIERTO)));
        when(periodoAcademicoMapper.toResponse(any(PeriodoAcademico.class)))
                .thenReturn(PeriodoAcademicoResponse.builder().id(1L).estado(EstadoPeriodo.ABIERTO).build());

        List<PeriodoAcademicoResponse> resultado = periodoAcademicoService.listar(EstadoPeriodo.ABIERTO);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo(EstadoPeriodo.ABIERTO);
        verify(periodoAcademicoRepository, never()).findAll();
    }
}
