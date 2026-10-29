package pe.edu.upeu.lamb.matricula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "periodo", nullable = false, length = 20)
    private String periodo;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoMatricula estado;

    @Column(name = "id_motivo_anulacion")
    private Long idMotivoAnulacion;

    @Builder.Default
    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleMatricula> detalles = new ArrayList<>();
}
