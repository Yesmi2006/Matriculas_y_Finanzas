package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "limites_creditos_ciclo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimiteCreditosPorCiclo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ciclo", nullable = false, unique = true)
    private Integer ciclo;

    @Column(name = "creditos_maximos", nullable = false)
    private Integer creditosMaximos;
}
