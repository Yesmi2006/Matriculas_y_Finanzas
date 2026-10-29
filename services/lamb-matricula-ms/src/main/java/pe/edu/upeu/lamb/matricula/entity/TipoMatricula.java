package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tipos_matricula")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "dias_plazo_extra", nullable = false)
    private Integer diasPlazoExtra;

    @Column(name = "recargo", nullable = false, precision = 5, scale = 2)
    private BigDecimal recargo;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
