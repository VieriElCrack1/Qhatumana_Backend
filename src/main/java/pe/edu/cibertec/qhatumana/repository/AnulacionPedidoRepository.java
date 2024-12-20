package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.AnulacionPedido;

@Repository
public interface AnulacionPedidoRepository extends JpaRepository<AnulacionPedido, Integer> {

}
