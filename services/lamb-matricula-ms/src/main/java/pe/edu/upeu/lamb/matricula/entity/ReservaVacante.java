package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reservas_vacante")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVacante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "id_seccion", nullable = false)
    private Long idSeccion;

    @Column(name = "fecha_reserva", nullable = false)
    private Instant fechaReserva;

    @Column(name = "fecha_expiracion", nullable = false)
    private Instant fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReserva estado;
}
