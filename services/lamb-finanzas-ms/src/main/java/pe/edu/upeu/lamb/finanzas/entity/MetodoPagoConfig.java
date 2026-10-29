package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "metodos_pago_config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coincide con el nombre del enum MetodoPago (TARJETA, YAPE, PLIN, TRANSFERENCIA, EFECTIVO).
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "comision", nullable = false, precision = 5, scale = 2)
    private BigDecimal comision;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
