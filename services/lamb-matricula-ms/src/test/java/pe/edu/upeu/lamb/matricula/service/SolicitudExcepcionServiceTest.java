package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionResponse;
import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionUpdateRequest;
import pe.edu.upeu.lamb.matricula.entity.EstadoSolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.entity.SolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.mapper.SolicitudExcepcionMapper;
import pe.edu.upeu.lamb.matricula.repository.SolicitudExcepcionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudExcepcionServiceTest {

    @Mock
    private SolicitudExcepcionRepository solicitudExcepcionRepository;
    @Mock
    private SolicitudExcepcionMapper solicitudExcepcionMapper;

    @InjectMocks
    private SolicitudExcepcionService solicitudExcepcionService;

    private SolicitudExcepcion solicitud(EstadoSolicitudExcepcion estado) {
        return SolicitudExcepcion.builder()
                .id(1L)
                .idPersona(100L)
                .idSeccion(5L)
                .idMatricula(1L)
                .motivo("Motivo original")
                .estado(estado)
                .fechaSolicitud(Instant.now())
                .build();
    }

    @Test
    void actualizar_editaElMotivo_siLaSolicitudEstaPendiente() {
        SolicitudExcepcion existente = solicitud(EstadoSolicitudExcepcion.PENDIENTE);
        when(solicitudExcepcionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(solicitudExcepcionRepository.save(any(SolicitudExcepcion.class))).thenAnswer(inv -> inv.getArgument(0));
        when(solicitudExcepcionMapper.toResponse(any(SolicitudExcepcion.class)))
                .thenAnswer(inv -> {
                    SolicitudExcepcion s = inv.getArgument(0);
                    return SolicitudExcepcionResponse.builder().id(s.getId()).motivo(s.getMotivo()).estado(s.getEstado()).build();
                });

        SolicitudExcepcionUpdateRequest request = new SolicitudExcepcionUpdateRequest();
        request.setMotivo("Motivo actualizado");

        SolicitudExcepcionResponse response = solicitudExcepcionService.actualizar(1L, request);

        assertThat(response.getMotivo()).isEqualTo("Motivo actualizado");
        verify(solicitudExcepcionRepository).save(existente);
    }

    @Test
    void actualizar_lanzaBusinessRuleException_siLaSolicitudYaFueResuelta() {
        when(solicitudExcepcionRepository.findById(1L)).thenReturn(Optional.of(solicitud(EstadoSolicitudExcepcion.APROBADA)));

        SolicitudExcepcionUpdateRequest request = new SolicitudExcepcionUpdateRequest();
        request.setMotivo("Intento de edicion");

        assertThatThrownBy(() -> solicitudExcepcionService.actualizar(1L, request))
                .isInstanceOf(BusinessRuleException.class);
    }
}
