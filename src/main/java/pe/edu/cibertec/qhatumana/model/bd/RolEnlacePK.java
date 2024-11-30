package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RolEnlacePK implements Serializable {
    private int idrol;
    private int idenlace;

    @Override
    public int hashCode() {
        return Objects.hash(idenlace, idrol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RolEnlacePK other = (RolEnlacePK) obj;
        return idenlace == other.idenlace && idrol == other.idrol;
    }

}
