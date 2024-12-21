package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT COALESCE(MAX(p.idpedido), 0) FROM Pedido p")
    int obtenerMaximoIdPedido();

    @Query("SELECT new pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse(p.idpedido,concat(p.cliente.nomcliente,' ', p.cliente.apecliente),concat(p.usuario.nomusuario,' ', p.usuario.apeusuario),p.descuento,p.montototal,p.direccion,p.estadoPedido.nomestado)" +
            " FROM Pedido p" +
            " JOIN p.detallePedidoList dp" +
            " JOIN dp.producto prod" +
            " WHERE LOWER(concat(p.cliente.nomcliente, ' ', p.cliente.apecliente)) LIKE LOWER(CONCAT('%', :nomcliente, '%'))" +
            " GROUP BY p.idpedido, p.cliente.nomcliente, p.cliente.apecliente, p.usuario.nomusuario, p.usuario.apeusuario, p.descuento, p.montototal, p.direccion, p.estadoPedido.nomestado")
    List<PedidoListResponse> consultarPedido(@Param("nomcliente") String nomcliente);

    @Query("SELECT new pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse(p.idpedido,concat(p.cliente.nomcliente,' ', p.cliente.apecliente),concat(p.usuario.nomusuario,' ', p.usuario.apeusuario),p.descuento,p.montototal,p.direccion,p.estadoPedido.nomestado)" +
            " FROM Pedido p" +
            " JOIN p.detallePedidoList dp" +
            " JOIN dp.producto prod" +
            " WHERE (LOWER(concat(p.cliente.nomcliente, ' ', p.cliente.apecliente)) LIKE LOWER(CONCAT('%', :cliente, '%')) OR :cliente IS NULL OR :cliente = '')" +
            " AND p.estadoPedido.idestado = :idestado" +
            " GROUP BY p.idpedido, p.cliente.nomcliente, p.cliente.apecliente, p.usuario.nomusuario, p.usuario.apeusuario, p.descuento, p.montototal, p.direccion, p.estadoPedido.nomestado" +
            " ORDER BY p.idpedido DESC")
    List<PedidoListResponse> consultarPedidoRegister(@Param("cliente") String cliente, @Param("idestado") Integer idestado);
}
