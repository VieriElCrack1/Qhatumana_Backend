package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "devolucionpedido")
public class DevolucionPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iddevolucion;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    private String motivodevolucion;
    @Temporal(TemporalType.DATE)
    private LocalDate fechadevolucion;
}
