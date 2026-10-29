package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "configuraciones_mora")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionMora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "dias_gracia", nullable = false)
    private Integer diasGracia;

    @Column(name = "porcentaje_mora_diario", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeMoraDiario;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
