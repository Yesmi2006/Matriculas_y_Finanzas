package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    // Matricula que origino el cargo (null para pagos que no vienen de un evento de matricula).
    // Unica: evita generar un cargo duplicado si el evento matricula.confirmada se reentrega.
    @Column(name = "matricula_id", unique = true)
    private Long matriculaId;

    @Column(name = "concepto", nullable = false, length = 150)
    private String concepto;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    // Nulo mientras el cargo esta PENDIENTE (recien generado, sin canal de pago elegido todavia).
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", length = 20)
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPago estado;

    @Column(name = "fecha_pago", nullable = false)
    private Instant fechaPago;

    @Column(name = "referencia_externa", length = 100)
    private String referenciaExterna;

    // Motivo de rechazo o reembolso (referencia blanda a MotivoPago); nulo si no aplica.
    @Column(name = "id_motivo_pago")
    private Long idMotivoPago;
}
