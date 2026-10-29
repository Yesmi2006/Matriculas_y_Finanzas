package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "secciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_curso", nullable = false)
    private Long idCurso;

    @Column(name = "id_periodo_academico", nullable = false)
    private Long idPeriodoAcademico;

    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSeccion estado;
}
