package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcliente;
    private String nomcliente;
    private String apecliente;
    private String dni;
    private String telefono;
    private String email;
    private LocalDate fechareg;
    private Boolean estado;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> estadoPedidoList;
}
