package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Factura;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaReportListResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    @Query("SELECT new pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaReportListResponse(concat(cl.nomcliente,' ',cl.apecliente),cl.email, " +
            " p.direccion,CAST(f.fechaemision AS java.util.Date),f.numfactura,ep.nomestado,pr.nomproducto,pr.descrip,c.nomcategoria," +
            " pd.cantidad,pd.precio,pd.subtotal, p.montototal,p.descuento,concat(u.nomusuario,' ',u.apeusuario)," +
            " u.username,metodo.nompago) " +
            " FROM Factura f " +
            " JOIN f.pedido p " +
            " JOIN p.estadoPedido ep " +
            " JOIN p.detallePedidoList pd " +
            " JOIN p.cliente cl" +
            " JOIN pd.producto pr " +
            " JOIN pr.categoria c " +
            " JOIN p.usuario u " +
            " JOIN p.pagoPedidoList pago " +
            " JOIN pago.metodoPago metodo " +
            " WHERE p.idpedido=:idpedido AND f.estadofactura = true " +
            " GROUP BY concat(cl.nomcliente,' ',cl.apecliente),cl.email, " +
            " p.direccion,f.fechaemision,f.numfactura,ep.nomestado,pr.nomproducto,pr.descrip,c.nomcategoria, " +
            " pd.cantidad,pd.precio,pd.subtotal, p.montototal,p.descuento,concat(u.nomusuario,' ',u.apeusuario), " +
            " u.username,metodo.nompago ")
    List<FacturaReportListResponse> consultarFacturaReportedPedido(@Param("idpedido") int idpedido);

    @Query("SELECT f.numfactura FROM Factura f ORDER BY f.idfactura DESC LIMIT 1")
    String obtenerUltimaPosicionNumFactura();

    boolean existsByPedidoAndEstadofactura(Pedido pedido, Boolean estadoFactura);

    Optional<Factura> findByPedido(Pedido pedido);
}
