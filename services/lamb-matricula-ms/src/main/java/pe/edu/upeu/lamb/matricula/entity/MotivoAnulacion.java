package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "motivos_anulacion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotivoAnulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
