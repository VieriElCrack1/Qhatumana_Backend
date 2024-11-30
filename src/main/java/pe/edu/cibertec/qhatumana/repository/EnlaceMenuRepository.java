package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.EnlaceMenu;

import java.util.List;

@Repository
public interface EnlaceMenuRepository extends JpaRepository<EnlaceMenu, Integer> {
    @Query("SELECT coalesce(max(idenlace), 0) + 1 FROM EnlaceMenu ")
    int obtenerIdEnlaceMenu();

    @Query("Select e from RolEnlace re join re.enlace e where re.rol.idrol=?1")
    List<EnlaceMenu> traerEnlaceUsuario(int codRol);
}
