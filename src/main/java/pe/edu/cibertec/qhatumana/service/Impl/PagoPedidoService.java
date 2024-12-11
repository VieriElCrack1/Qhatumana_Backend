package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.DetallePedido;
import pe.edu.cibertec.qhatumana.model.bd.MetodoPago;
import pe.edu.cibertec.qhatumana.model.bd.PagoPedido;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago.PagoPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.detalle.DetallePedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.MetodoPagoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoResponse;
import pe.edu.cibertec.qhatumana.repository.MetodoPagoRepository;
import pe.edu.cibertec.qhatumana.repository.PagoPedidoRepository;
import pe.edu.cibertec.qhatumana.repository.PedidoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IPagoPedidoService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PagoPedidoService implements IPagoPedidoService {

    private final PagoPedidoRepository pagoPedidoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public List<PagoPedidoResponse> listadoPagoPedido() {
        return convertirListPagoPedidoResponse(pagoPedidoRepository.findAll());
    }

    @Override
    public ResponseAPI<PagoPedidoResponse> registrarPagoPedido(PagoPedidoRequest request) {
        try{
            PagoPedido pagoPedido = new PagoPedido();

            Pedido pedido = pedidoRepository.findById(request.getIdpedido()).orElseThrow(() ->
                    new ResourceNotFoundException("No se encontro el pedido"));
            boolean existePedidoPagado = pagoPedidoRepository.existsByPedidoIdpedidoAndEstadoTrue(pedido.getIdpedido());

            if (existePedidoPagado) {
                return ResponseAPI.<PagoPedidoResponse>builder()
                        .message("Ya existe un pago registrado para este pedido")
                        .status("ERROR")
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.name())
                        .build();
            }
            pagoPedido.setPedido(pedido);
            pagoPedido.setMontopagado(pedido.getMontototal());

            MetodoPago metodoPago = metodoPagoRepository.findById(request.getIdmetodoPago()).orElseThrow(() ->
                    new ResourceNotFoundException("No se encontro ningun metodo de pago"));
            pagoPedido.setMetodoPago(metodoPago);

            pagoPedido.setFechapago(LocalDate.now());
            pagoPedido.setEstado(true);

            PagoPedido save = pagoPedidoRepository.save(pagoPedido);
            return ResponseAPI.<PagoPedidoResponse>builder()
                    .message("Pago pedido registrado exitosamente")
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .data(convertirPagoPedidoResponse(save))
                    .build();
        }catch (DataAccessException exception) {
            return ResponseAPI.<PagoPedidoResponse>builder()
                    .message("No se pudo registrar el pago de pedido, no se pudo acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .messageDescription(exception.getMessage())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
        }catch (Exception e) {
            return ResponseAPI.<PagoPedidoResponse>builder()
                    .message("No se pudo registrar el pago de pedido")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .messageDescription(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .build();
        }
    }

    //List Pago pedido
    private List<PagoPedidoResponse> convertirListPagoPedidoResponse(List<PagoPedido> pagoPedidos) {
        List<PagoPedidoResponse> response = new ArrayList<>();
        for (PagoPedido pagoPedido : pagoPedidos) {
            response.add(convertirPagoPedidoResponse(pagoPedido));
        }
        return response;
    }

    //Pago pedido
    private PagoPedidoResponse convertirPagoPedidoResponse(PagoPedido pagoPedido) {
        return PagoPedidoResponse.builder()
                .idpago(pagoPedido.getIdpago())
                .pedido(convertirPedidoResponse(pagoPedido.getPedido()))
                .montopagado(pagoPedido.getMontopagado())
                .idmetodopago(convertirMetodoPagoResponse(pagoPedido.getMetodoPago()))
                .estadopago(pagoPedido.getEstado() ? "Pagado" : "Rechazado")
                .build();
    }

    //metodo pago
    private MetodoPagoResponse convertirMetodoPagoResponse(MetodoPago metodoPago) {
        return MetodoPagoResponse.builder()
                .idmetodopago(metodoPago.getIdmetodopago())
                .nommetodopago(metodoPago.getNompago())
                .build();
    }

    //pedido
    private PedidoResponse convertirPedidoResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .idpedido(pedido.getIdpedido())
                .idcliente(pedido.getCliente().getIdcliente())
                .cliente(pedido.getCliente().getNomcliente() + " " + pedido.getCliente().getApecliente())
                .idusuario(pedido.getUsuario().getIdusuario())
                .usuario(pedido.getUsuario().getNomusuario() + " " + pedido.getUsuario().getApeusuario())
                .fechareg(pedido.getFechapedido())
                .igv(pedido.getIgv())
                .descuento(pedido.getDescuento())
                .montototal(pedido.getMontototal())
                .direccion(pedido.getDireccion())
                .idestado(pedido.getEstadoPedido().getIdestado())
                .nomestado(pedido.getEstadoPedido().getNomestado())
                .detalles(convertirDetallePedidoListResponse(pedido.getDetallePedidoList()))
                .build();
    }

    private List<DetallePedidoResponse> convertirDetallePedidoListResponse(List<DetallePedido> detallePedidos) {
        List<DetallePedidoResponse> response = new ArrayList<>();
        for (DetallePedido detallePedido : detallePedidos) {
            response.add(convertirDetallePedidoResponse(detallePedido));
        }
        return response;
    }

    private DetallePedidoResponse convertirDetallePedidoResponse(DetallePedido detallePedido) {
        return DetallePedidoResponse.builder()
                .iddetallepedido(detallePedido.getIdpedidodetalle())
                .idproducto(detallePedido.getProducto().getIdproducto())
                .nomprod(detallePedido.getProducto().getNomproducto())
                .cantidad(detallePedido.getCantidad())
                .precioUnitario(detallePedido.getPrecio())
                .subtotal(detallePedido.getSubtotal())
                .build();
    }
}
