package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transacciones_pasarela")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionPasarela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @Column(name = "referencia", nullable = false, length = 100)
    private String referencia;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoTransaccionPasarela estado;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;
}
