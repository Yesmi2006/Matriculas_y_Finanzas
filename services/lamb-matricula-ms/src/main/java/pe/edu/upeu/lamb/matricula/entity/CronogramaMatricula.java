package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cronogramas_matricula")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronogramaMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_periodo_academico", nullable = false)
    private Long idPeriodoAcademico;

    @Column(name = "id_tipo_matricula", nullable = false)
    private Long idTipoMatricula;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
}
