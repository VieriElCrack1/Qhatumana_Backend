package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpedido;
    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    private LocalDate fechareg;
    private BigDecimal igv;
    private BigDecimal descuento;
    private BigDecimal montototal;
    private String direccion;
    @ManyToOne
    @JoinColumn(name = "idestado", nullable = false)
    private EstadoPedido estadoPedido;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detallePedidoList;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<PagoPedido> pagoPedidoList;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<Factura> facturaList;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<AnulacionPedido> anulacionPedidoList;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<DevolucionPedido> devolucionPedidoList;
}
