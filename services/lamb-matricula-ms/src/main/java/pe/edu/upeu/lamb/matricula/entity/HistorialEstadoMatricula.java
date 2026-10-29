package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "historial_estado_matricula")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstadoMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_matricula", nullable = false)
    private Long idMatricula;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 20)
    private EstadoMatricula estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 20)
    private EstadoMatricula estadoNuevo;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;
}
