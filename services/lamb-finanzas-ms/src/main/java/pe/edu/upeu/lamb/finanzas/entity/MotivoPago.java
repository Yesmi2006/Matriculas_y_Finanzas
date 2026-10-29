package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "motivos_pago")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotivoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoMotivoPago tipo;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
