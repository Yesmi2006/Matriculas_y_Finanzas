package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "descuentos_aplicados")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoAplicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    // Pago al que se aplico el descuento; nulo mientras no se asocie a un cargo concreto.
    @Column(name = "id_pago")
    private Long idPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoDescuento tipo;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "fecha_aplicacion", nullable = false)
    private Instant fechaAplicacion;
}
