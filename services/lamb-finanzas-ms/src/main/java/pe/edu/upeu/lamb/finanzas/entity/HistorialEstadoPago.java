package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "historial_estado_pago")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstadoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 20)
    private EstadoPago estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 20)
    private EstadoPago estadoNuevo;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;
}
