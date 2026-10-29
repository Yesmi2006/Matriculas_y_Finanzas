package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.MotivoPago;
import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import pe.edu.upeu.lamb.finanzas.mapper.MotivoPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.MotivoPagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MotivoPagoServiceTest {

    @Mock
    private MotivoPagoRepository motivoPagoRepository;
    @Mock
    private MotivoPagoMapper motivoPagoMapper;

    @InjectMocks
    private MotivoPagoService motivoPagoService;

    private MotivoPago motivo(Long id, TipoMotivoPago tipo, boolean vigente) {
        return MotivoPago.builder()
                .id(id)
                .tipo(tipo)
                .codigo("MOT-" + id)
                .descripcion("Motivo " + id)
                .vigente(vigente)
                .build();
    }

    private void stubMapper() {
        when(motivoPagoMapper.toResponse(any(MotivoPago.class)))
                .thenAnswer(inv -> {
                    MotivoPago m = (MotivoPago) inv.getArgument(0);
                    return MotivoPagoResponse.builder()
                            .id(m.getId())
                            .tipo(m.getTipo())
                            .vigente(m.isVigente())
                            .build();
                });
    }

    @Test
    void listar_sinFiltros_devuelveTodosLosMotivos() {
        when(motivoPagoRepository.findAll()).thenReturn(List.of(
                motivo(1L, TipoMotivoPago.RECHAZO, true),
                motivo(2L, TipoMotivoPago.REEMBOLSO, false)));
        stubMapper();

        List<MotivoPagoResponse> resultado = motivoPagoService.listar(null, null);

        assertThat(resultado).hasSize(2);
        verify(motivoPagoRepository, never()).findByTipo(any());
    }

    @Test
    void listar_combinaTipoYVigente() {
        when(motivoPagoRepository.findByTipo(TipoMotivoPago.RECHAZO)).thenReturn(List.of(
                motivo(1L, TipoMotivoPago.RECHAZO, true),
                motivo(3L, TipoMotivoPago.RECHAZO, false)));
        stubMapper();

        List<MotivoPagoResponse> resultado = motivoPagoService.listar(TipoMotivoPago.RECHAZO, true);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
    }
}
