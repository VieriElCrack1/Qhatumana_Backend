package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "metodopago")
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idmetodopago;
    private String nompago;

    @JsonIgnore
    @OneToMany(mappedBy = "metodoPago")
    private List<PagoPedido> pagoPedidoList;
}
