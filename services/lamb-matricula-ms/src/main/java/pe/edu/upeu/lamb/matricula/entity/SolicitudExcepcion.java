package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "solicitudes_excepcion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudExcepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "id_seccion")
    private Long idSeccion;

    @Column(name = "id_matricula")
    private Long idMatricula;

    @Column(name = "motivo", nullable = false, length = 250)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitudExcepcion estado;

    @Column(name = "fecha_solicitud", nullable = false)
    private Instant fechaSolicitud;
}
