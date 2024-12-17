package pe.edu.cibertec.qhatumana.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.anulacion.AnularPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion.AnularPedidoResponse;
import pe.edu.cibertec.qhatumana.repository.*;
import pe.edu.cibertec.qhatumana.service.interfaces.IAnularPedidoService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnularPedidoService implements IAnularPedidoService {

    private final AnulacionPedidoRepository anulacionPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final ProductoRepository productoRepository;
    private final FacturaRepository facturaRepository;
    private final PagoPedidoRepository pagoPedidoRepository;

    @Override
    public List<AnularPedidoResponse> listadoAnularPedido() {
        return convertirListAnularPedidoResponse(anulacionPedidoRepository.findAll());
    }

    @Modifying
    @Transactional
    @Override
    public ResponseAPI<AnularPedidoResponse> anularPedido(AnularPedidoRequest request) {
        AnulacionPedido pedidosave;
        try {
            AnulacionPedido anulacionPedido = new AnulacionPedido();
            Pedido pedido = pedidoRepository.findById(request.getIdpedido())
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con ID: " + request.getIdpedido()));

            if (pedido.getEstadoPedido().getNomestado().equalsIgnoreCase("Anulado")) {
                return ResponseAPI.<AnularPedidoResponse>builder()
                        .message("El pedido ya está anulado")
                        .status("ERROR")
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.name())
                        .build();
            }

            Factura factura = facturaRepository.findByPedido(pedido).orElseThrow(() -> new ResourceNotFoundException("No se encontro ninguna factura asociada al pedido"));
            PagoPedido pagoPedido = pagoPedidoRepository.findByPedido(pedido).orElseThrow(() -> new ResourceNotFoundException("No se encontro ninguna pago del pedido"));

            anulacionPedido.setPedido(pedido);
            anulacionPedido.setMotivoanulacion(request.getMotivoanulacion());
            anulacionPedido.setFechaanulacion(LocalDate.now());

            pedidosave = anulacionPedidoRepository.save(anulacionPedido);

            devolverStock(pedido.getDetallePedidoList());

            EstadoPedido estadoAnulado = estadoPedidoRepository.findByNomestado("Anulado")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Anulado' no encontrado"));
            pedido.setEstadoPedido(estadoAnulado);
            pedidoRepository.save(pedido);

            if(factura.getEstadofactura()) {
                factura.setEstadofactura(false);
                facturaRepository.save(factura);
            }

            if(pagoPedido.getEstado()) {
                pagoPedido.setEstado(false);
                pagoPedidoRepository.save(pagoPedido);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseAPI.<AnularPedidoResponse>builder()
                    .message("Error al anular el pedido: " + e.getMessage())
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
        return ResponseAPI.<AnularPedidoResponse>builder()
                .message("Pedido anulado y stock devuelto exitosamente")
                .status("EXITO")
                .data(convertirAnularPedidoResponse(pedidosave))
                .httpStatus(HttpStatus.OK.value())
                .build();
    }

    private List<AnularPedidoResponse> convertirListAnularPedidoResponse(List<AnulacionPedido> anulacionPedidos) {
        List<AnularPedidoResponse> response = new ArrayList<>();
        for (AnulacionPedido anulacionPedido : anulacionPedidos) {
            response.add(convertirAnularPedidoResponse(anulacionPedido));
        }
        return response;
    }

    private AnularPedidoResponse convertirAnularPedidoResponse(AnulacionPedido anulacionPedido) {
        return AnularPedidoResponse.builder()
                .idanulacion(anulacionPedido.getIdanulacion())
                .idpedido(anulacionPedido.getPedido().getIdpedido())
                .motivoanulacion(anulacionPedido.getMotivoanulacion())
                .fechaanulacion(anulacionPedido.getFechaanulacion())
                .build();
    }

    private void devolverStock(List<DetallePedido> detalles) {
        for (DetallePedido detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
    }
}
