package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "enlacemenu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnlaceMenu implements Serializable {
    @Id
    private Integer idenlace;
    private String descripcion;
    private String ruta;
    private String icon;

    @ManyToOne
    @JoinColumn(name = "padreid")
    private EnlaceMenu padreid;

    @JsonIgnore
    @OneToMany(mappedBy = "enlace")
    private List<RolEnlace> listarolenlace;

}
