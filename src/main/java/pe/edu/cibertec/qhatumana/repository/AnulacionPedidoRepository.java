package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.AnulacionPedido;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion.AnularPedidoConsultaResponse;

import java.util.List;

@Repository
public interface AnulacionPedidoRepository extends JpaRepository<AnulacionPedido, Integer> {

    @Query("SELECT new pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion.AnularPedidoConsultaResponse(ap.idanulacion,concat(c.nomcliente,' ',c.apecliente),p.montototal,p.direccion,CAST(ap.fechaanulacion AS java.util.Date),p.estadoPedido.nomestado)" +
            " FROM AnulacionPedido ap" +
            " JOIN ap.pedido.cliente c" +
            " JOIN ap.pedido p" +
            " WHERE (LOWER(concat(c.nomcliente, ' ', c.apecliente)) LIKE LOWER(CONCAT('%', :cliente, '%')) OR :cliente IS NULL OR :cliente = '')" +
            " GROUP BY ap.idanulacion,c.nomcliente,c.apecliente,p.montototal,p.direccion,ap.fechaanulacion,p.estadoPedido.nomestado")
    List<AnularPedidoConsultaResponse> consularAnulacionPedidoXCliente(@Param("cliente") String cliente);
}
