package pe.edu.cibertec.qhatumana.model.dto.request.pedido;

import lombok.Builder;
import lombok.Data;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.detalle.DetallePedidoUpdateRequest;

import java.util.List;

@Builder
@Data
public class PedidoAndDetalleUpdateRequest {
    private Integer idpedido;
    private Integer idcliente;
    private Integer idusuario;
    private Double descuento;
    private String direccion;
    private Integer idestado;
    private List<DetallePedidoUpdateRequest> detalles;
}
