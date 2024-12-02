package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "anulacionpedido")
public class AnulacionPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idanulacion;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    private String motivoanulacion;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaanulacion;
}
