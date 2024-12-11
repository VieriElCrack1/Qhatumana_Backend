package pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagoPedidoRequest {
    private Integer idpedido;
    private Integer idmetodoPago;
}
