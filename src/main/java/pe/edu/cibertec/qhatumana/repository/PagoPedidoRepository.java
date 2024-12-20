package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.PagoPedido;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoConsultaResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoPedidoRepository extends JpaRepository<PagoPedido, Integer> {
    boolean existsByPedidoIdpedidoAndEstadoTrue(Integer idpedido);

    Optional<PagoPedido> findByPedido(Pedido pedido);

    @Query("SELECT new pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoConsultaResponse(pd.idpago,concat(c.nomcliente,' ',c.apecliente),pd.montopagado,pd.metodoPago.nompago,CASE WHEN pd.estado = true THEN 'Activo' ELSE 'Inactivo' END)" +
            " FROM PagoPedido pd" +
            " JOIN pd.pedido.cliente c" +
            " WHERE (LOWER(concat(c.nomcliente, ' ', c.apecliente)) LIKE LOWER(CONCAT('%', :cliente, '%')) OR :cliente IS NULL OR :cliente = '')" +
            " GROUP BY pd.idpago,c.nomcliente,c.apecliente,pd.montopagado,pd.metodoPago.nompago")
    List<PagoPedidoConsultaResponse> consultarPagoPedidoXnomcliente(@Param("cliente") String cliente);
}
