package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "rolenlace")
public class RolEnlace {
    @EmbeddedId
    private RolEnlacePK pk;

    @ManyToOne
    @JoinColumn(name = "idrol", insertable = false, updatable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "idenlace", insertable = false, updatable = false)
    private EnlaceMenu enlace;

}