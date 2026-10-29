package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "conceptos_pago")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador de negocio estable (ej. "MATRICULA") usado por otros procesos para
    // encontrar el monto vigente sin depender del id numerico.
    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
