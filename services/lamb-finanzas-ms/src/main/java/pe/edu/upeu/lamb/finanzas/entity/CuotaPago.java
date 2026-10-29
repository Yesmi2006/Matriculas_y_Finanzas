package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cuotas_pago")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuotaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;

    @Column(name = "monto_cuota", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoCuota;

    @Column(name = "fecha_vencimiento", nullable = false)
    private Instant fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCuota estado;

    @Column(name = "fecha_pago")
    private Instant fechaPago;
}
