package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "devolucionpedido")
public class DevolucionProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iddevolucion;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "idproducto", nullable = false)
    private Producto producto;
    private Integer cantidaddevolucion;
    private String motivodevolucion;
    @Temporal(TemporalType.DATE)
    private LocalDate fechadevolucion;
}
