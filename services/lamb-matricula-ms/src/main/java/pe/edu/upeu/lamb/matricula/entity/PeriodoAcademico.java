package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodos_academicos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "fecha_limite_matricula", nullable = false)
    private LocalDate fechaLimiteMatricula;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPeriodo estado;
}
