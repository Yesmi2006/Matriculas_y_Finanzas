package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.SeccionResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import pe.edu.upeu.lamb.matricula.entity.Seccion;
import pe.edu.upeu.lamb.matricula.mapper.SeccionMapper;
import pe.edu.upeu.lamb.matricula.repository.DetalleMatriculaRepository;
import pe.edu.upeu.lamb.matricula.repository.SeccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeccionServiceTest {

    @Mock
    private SeccionRepository seccionRepository;
    @Mock
    private DetalleMatriculaRepository detalleMatriculaRepository;
    @Mock
    private SeccionMapper seccionMapper;

    @InjectMocks
    private SeccionService seccionService;

    private Seccion seccion(Long id, Long idPeriodo, EstadoSeccion estado) {
        return Seccion.builder()
                .id(id)
                .idCurso(900L)
                .idPeriodoAcademico(idPeriodo)
                .cupoMaximo(30)
                .estado(estado)
                .build();
    }

    private void stubMapperYOcupacion() {
        when(detalleMatriculaRepository.contarMatriculadosEnSeccion(any(), any(Iterable.class))).thenReturn(0L);
        when(seccionMapper.toResponse(any(Seccion.class), org.mockito.ArgumentMatchers.anyLong()))
                .thenAnswer(inv -> SeccionResponse.builder()
                        .id(((Seccion) inv.getArgument(0)).getId())
                        .estado(((Seccion) inv.getArgument(0)).getEstado())
                        .idPeriodoAcademico(((Seccion) inv.getArgument(0)).getIdPeriodoAcademico())
                        .build());
    }

    @Test
    void listar_sinFiltros_devuelveTodasLasSecciones() {
        when(seccionRepository.findAll()).thenReturn(List.of(
                seccion(1L, 10L, EstadoSeccion.ABIERTA),
                seccion(2L, 20L, EstadoSeccion.CERRADA)));
        stubMapperYOcupacion();

        List<SeccionResponse> resultado = seccionService.listar(null, null);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void listar_combinaIdPeriodoAcademicoYEstado() {
        when(seccionRepository.findByIdPeriodoAcademico(10L)).thenReturn(List.of(
                seccion(1L, 10L, EstadoSeccion.ABIERTA),
                seccion(2L, 10L, EstadoSeccion.CERRADA)));
        stubMapperYOcupacion();

        List<SeccionResponse> resultado = seccionService.listar(10L, EstadoSeccion.ABIERTA);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
    }
}
