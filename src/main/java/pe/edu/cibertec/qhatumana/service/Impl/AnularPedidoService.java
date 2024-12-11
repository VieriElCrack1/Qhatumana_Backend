package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.DetallePedido;
import pe.edu.cibertec.qhatumana.model.bd.EstadoPedido;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.bd.Producto;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.repository.EstadoPedidoRepository;
import pe.edu.cibertec.qhatumana.repository.PedidoRepository;
import pe.edu.cibertec.qhatumana.repository.ProductoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IAnularPedido;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnularPedidoService implements IAnularPedido {

    private final PedidoRepository pedidoRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public ResponseAPI<Void> anularPedido(int idpedido) {
        try {
            Pedido pedido = pedidoRepository.findById(idpedido)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con ID: " + idpedido));

            if (pedido.getEstadoPedido().getNomestado().equalsIgnoreCase("Anulado")) {
                return ResponseAPI.<Void>builder()
                        .message("El pedido ya está anulado")
                        .status("ERROR")
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.name())
                        .build();
            }
            devolverStock(pedido.getDetallePedidoList());

            EstadoPedido estadoAnulado = estadoPedidoRepository.findByNomestado("Anulado")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Anulado' no encontrado"));
            pedido.setEstadoPedido(estadoAnulado);
            pedidoRepository.save(pedido);

        } catch (Exception e) {
            return ResponseAPI.<Void>builder()
                    .message("Error al anular el pedido: " + e.getMessage())
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
        }

        return ResponseAPI.<Void>builder()
                .message("Pedido anulado y stock devuelto exitosamente")
                .status("EXITO")
                .httpStatus(HttpStatus.OK.value())
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
