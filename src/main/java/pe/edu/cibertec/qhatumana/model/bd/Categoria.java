package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcategoria;
    private String nomcategoria;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productoList;
}
