package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pagopedido")
public class PagoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpago;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    private BigDecimal montopagado;
    @ManyToOne
    @JoinColumn(name = "idmetodopago", nullable = false)
    private MetodoPago metodoPago;
    @Temporal(TemporalType.DATE)
    private LocalDate fechareg;
}
