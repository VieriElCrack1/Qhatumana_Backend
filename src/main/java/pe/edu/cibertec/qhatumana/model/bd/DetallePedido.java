package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "detallepedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpedidodetalle;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "idproducto", nullable = false)
    private Producto producto;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal subtotal;
}
