package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "planes_refinanciamiento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanRefinanciamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "id_pago_original", nullable = false)
    private Long idPagoOriginal;

    @Column(name = "numero_cuotas", nullable = false)
    private Integer numeroCuotas;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPlanRefinanciamiento estado;
}
