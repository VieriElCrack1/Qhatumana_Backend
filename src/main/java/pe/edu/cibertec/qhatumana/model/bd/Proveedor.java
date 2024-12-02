package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idproveedor;
    private String nomproveedor;
    private String ruc;
    private String email;
    private String telefono;
    private String direccion;
    private String fechareg;
    private Boolean estado;

    @JsonIgnore
    @OneToMany(mappedBy = "proveedor")
    private List<Producto> productoList;
}
