package pe.edu.cibertec.qhatumana.repository;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.qhatumana.model.bd.Rol;

@Registered
public interface RolRepository extends JpaRepository<Rol, Integer> {
}
