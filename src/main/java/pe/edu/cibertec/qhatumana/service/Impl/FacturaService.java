package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.DetallePedido;
import pe.edu.cibertec.qhatumana.model.bd.Factura;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaUpdatePedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.detalle.DetallePedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaPedidoResponse;
import pe.edu.cibertec.qhatumana.repository.FacturaRepository;
import pe.edu.cibertec.qhatumana.repository.PedidoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IFacturaService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FacturaService implements IFacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public List<FacturaPedidoResponse> listaFacturaPedidoResponse() {
        return convertirListFacturaPedidoResponse(facturaRepository.findAll());
    }

    @Override
    public ResponseAPI<FacturaPedidoResponse> guardarFacturaPedidoResponse(FacturaPedidoRequest facturaPedidoRequest) {
        try {
            Factura factura = new Factura();

            Pedido pedido = pedidoRepository.findById(facturaPedidoRequest.getIdpedido()).orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido"));
            factura.setPedido(pedido);
            factura.setNumfactura(facturaPedidoRequest.getNumfactura());
            factura.setFechaemision(LocalDate.now());
            factura.setMontototal(pedido.getMontototal());
            factura.setEstadofactura(true);

            Factura facturaSave = facturaRepository.save(factura);

            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Factura registrada exitosamente")
                    .data(convertirFacturaPedidoResponse(facturaSave))
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .build();
        }catch (DataAccessException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("No se pudo acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (NullPointerException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Los valores de la factura son nulos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (Exception e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Hubo un error al registrar la factura")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseAPI<FacturaPedidoResponse> updateFacturaUpdatePedidoReques(FacturaUpdatePedidoRequest request) {
        try {
            Factura factura = new Factura();
            factura.setEstadofactura(request.getEstadofactura());

            Factura facturaSave = facturaRepository.save(factura);

            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Factura registrada exitosamente")
                    .data(convertirFacturaPedidoResponse(facturaSave))
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .build();
        }catch (DataAccessException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("No se pudo acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (NullPointerException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Los valores de la factura son nulos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (Exception e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Hubo un error al registrar la factura")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
    }

    private List<FacturaPedidoResponse> convertirListFacturaPedidoResponse(List<Factura> facturas) {
        List<FacturaPedidoResponse> response = new ArrayList<>();
        for (Factura factura : facturas) {
            response.add(convertirFacturaPedidoResponse(factura));
        }
        return response;
    }

    private FacturaPedidoResponse convertirFacturaPedidoResponse(Factura factura) {
        return FacturaPedidoResponse.builder()
                .idfactura(factura.getIdfactura())
                .pedido(convertirPedidoResponse(factura.getPedido()))
                .numfactura(factura.getNumfactura())
                .fechaemision(factura.getFechaemision())
                .montototal(factura.getMontototal())
                .estadofactura(factura.getEstadofactura() ? "Emitida" : "Anulada")
                .build();
    }

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