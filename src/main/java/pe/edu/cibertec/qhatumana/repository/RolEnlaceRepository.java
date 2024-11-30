package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.RolEnlace;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolEnlaceRepository extends JpaRepository<RolEnlace, Integer> {

    Optional<RolEnlace> findByPkIdrolAndPkIdenlace(Integer idrol, Integer idenlace);

    List<RolEnlace> findByPkIdrol(Integer idrol);
}
