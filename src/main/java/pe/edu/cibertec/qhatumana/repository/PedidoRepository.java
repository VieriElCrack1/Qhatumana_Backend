package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
