package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
