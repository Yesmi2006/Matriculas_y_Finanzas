package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.MetodoPagoConfigRequest;
import pe.edu.upeu.lamb.finanzas.dto.MetodoPagoConfigResponse;
import pe.edu.upeu.lamb.finanzas.entity.MetodoPagoConfig;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagoConfigMapper {

    public MetodoPagoConfig toEntity(MetodoPagoConfigRequest request) {
        return MetodoPagoConfig.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .comision(request.getComision())
                .vigente(request.getVigente() == null || request.getVigente())
                .build();
    }

    public void actualizar(MetodoPagoConfig config, MetodoPagoConfigRequest request) {
        config.setCodigo(request.getCodigo());
        config.setNombre(request.getNombre());
        config.setComision(request.getComision());
        if (request.getVigente() != null) {
            config.setVigente(request.getVigente());
        }
    }

    public MetodoPagoConfigResponse toResponse(MetodoPagoConfig config) {
        return MetodoPagoConfigResponse.builder()
                .id(config.getId())
                .codigo(config.getCodigo())
                .nombre(config.getNombre())
                .comision(config.getComision())
                .vigente(config.isVigente())
                .build();
    }
}
