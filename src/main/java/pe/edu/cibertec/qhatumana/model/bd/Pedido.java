package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    private LocalDate fechapedido;
    private Double igv;
    private Double descuento;
    private Double montototal;
    private String direccion;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestado", nullable = false)
    private EstadoPedido estadoPedido;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
    private List<DevolucionProducto> devolucionPedidoList;
}
