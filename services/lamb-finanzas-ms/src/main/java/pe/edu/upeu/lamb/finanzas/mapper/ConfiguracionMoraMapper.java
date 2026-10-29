package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.ConfiguracionMoraRequest;
import pe.edu.upeu.lamb.finanzas.dto.ConfiguracionMoraResponse;
import pe.edu.upeu.lamb.finanzas.entity.ConfiguracionMora;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionMoraMapper {

    public ConfiguracionMora toEntity(ConfiguracionMoraRequest request) {
        return ConfiguracionMora.builder()
                .codigo(request.getCodigo())
                .diasGracia(request.getDiasGracia())
                .porcentajeMoraDiario(request.getPorcentajeMoraDiario())
                .vigente(request.getVigente() == null || request.getVigente())
                .build();
    }

    public void actualizar(ConfiguracionMora config, ConfiguracionMoraRequest request) {
        config.setCodigo(request.getCodigo());
        config.setDiasGracia(request.getDiasGracia());
        config.setPorcentajeMoraDiario(request.getPorcentajeMoraDiario());
        if (request.getVigente() != null) {
            config.setVigente(request.getVigente());
        }
    }

    public ConfiguracionMoraResponse toResponse(ConfiguracionMora config) {
        return ConfiguracionMoraResponse.builder()
                .id(config.getId())
                .codigo(config.getCodigo())
                .diasGracia(config.getDiasGracia())
                .porcentajeMoraDiario(config.getPorcentajeMoraDiario())
                .vigente(config.isVigente())
                .build();
    }
}
