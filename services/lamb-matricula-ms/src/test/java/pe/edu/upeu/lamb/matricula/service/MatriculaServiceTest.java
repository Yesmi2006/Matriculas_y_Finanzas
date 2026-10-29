package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.DetalleMatricula;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.entity.Matricula;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.DetalleMatriculaMapper;
import pe.edu.upeu.lamb.matricula.mapper.MatriculaMapper;
import pe.edu.upeu.lamb.matricula.messaging.MatriculaEventosProducer;
import pe.edu.upeu.lamb.matricula.repository.MatriculaRepository;
import pe.edu.upeu.lamb.matricula.repository.SeccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private MatriculaMapper matriculaMapper;
    @Mock
    private DetalleMatriculaMapper detalleMatriculaMapper;
    @Mock
    private MatriculaEventosProducer producer;
    @Mock
    private SeccionRepository seccionRepository;
    @Mock
    private HistorialEstadoMatriculaService historialEstadoMatriculaService;
    @Mock
    private MotivoAnulacionService motivoAnulacionService;

    @InjectMocks
    private MatriculaService matriculaService;

    private Matricula matriculaConDetalle(Long detalleId) {
        return Matricula.builder()
                .id(1L)
                .idPersona(100L)
                .periodo("2026-1")
                .fecha(Instant.now())
                .estado(EstadoMatricula.REGISTRADA)
                .detalles(new ArrayList<>(List.of(
                        DetalleMatricula.builder().id(detalleId).idSeccion(5L).creditos(4).build())))
                .build();
    }

    @Test
    void obtenerDetalle_devuelveElDetalleDeEsaMatricula() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matriculaConDetalle(10L)));
        when(detalleMatriculaMapper.toResponse(any(DetalleMatricula.class)))
                .thenReturn(DetalleMatriculaResponse.builder().id(10L).idSeccion(5L).creditos(4).build());

        DetalleMatriculaResponse response = matriculaService.obtenerDetalle(1L, 10L);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getIdSeccion()).isEqualTo(5L);
    }

    @Test
    void obtenerDetalle_lanzaNotFound_siElDetalleEsDeOtraMatricula() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matriculaConDetalle(10L)));

        assertThatThrownBy(() -> matriculaService.obtenerDetalle(1L, 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void obtenerDetalle_lanzaNotFound_siLaMatriculaNoExiste() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matriculaService.obtenerDetalle(1L, 10L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
