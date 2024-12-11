package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.EstadoPedido;

import java.util.Optional;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Integer> {
    Optional<EstadoPedido> findByNomestado(String nomestado);
}
