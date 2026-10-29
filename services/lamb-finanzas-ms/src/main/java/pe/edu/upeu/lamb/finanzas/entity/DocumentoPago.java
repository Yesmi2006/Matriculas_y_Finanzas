package pe.edu.upeu.lamb.finanzas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "documentos_pago")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoDocumento tipo;

    @Column(name = "numero", nullable = false, unique = true, length = 50)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoDocumento estado;

    @Column(name = "fecha_emision", nullable = false)
    private Instant fechaEmision;
}
