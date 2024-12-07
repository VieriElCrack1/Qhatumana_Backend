package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "estadopedido")
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idestado;
    private String nomestado;

    @OneToMany(mappedBy = "estadoPedido")
    private List<Pedido> pedidoList;
}
