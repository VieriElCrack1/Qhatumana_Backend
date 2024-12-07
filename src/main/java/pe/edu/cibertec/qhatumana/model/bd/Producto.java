package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idproducto;
    private String nomproducto;
    private String descrip;
    @ManyToOne
    @JoinColumn(name = "idcategoria", nullable = false)
    private Categoria categoria;
    private Double precio;
    private Integer stock;
    private LocalDate fechareg;
    @ManyToOne
    @JoinColumn(name = "idproveedor", nullable = false)
    private Proveedor proveedor;
    private String urlproducto;
    private String urlnombre;
    private Boolean estado;

    @JsonIgnore
    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallePedidoList;
}
