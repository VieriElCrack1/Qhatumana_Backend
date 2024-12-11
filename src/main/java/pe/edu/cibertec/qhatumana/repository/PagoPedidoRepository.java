package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.PagoPedido;

@Repository
public interface PagoPedidoRepository extends JpaRepository<PagoPedido, Integer> {
    boolean existsByPedidoIdpedidoAndEstadoTrue(Integer idpedido);
}
