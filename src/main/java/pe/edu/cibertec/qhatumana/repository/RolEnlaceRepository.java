package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.RolEnlace;

@Repository
public interface RolEnlaceRepository extends JpaRepository<RolEnlace, Integer> {
}
