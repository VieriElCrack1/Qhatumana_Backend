package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Rol {
    @Id
    private Integer idrol;
    private String nomrol;

    @JsonIgnore
    @OneToMany(mappedBy = "rol")
    private List<Usuario> listaU;

    @JsonIgnore
    @OneToMany(mappedBy = "rol")
    private List<RolEnlace> listarolEnlace;

}