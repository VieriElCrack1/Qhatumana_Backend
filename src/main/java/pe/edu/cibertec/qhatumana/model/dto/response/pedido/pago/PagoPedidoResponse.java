package pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago;

import lombok.Builder;
import lombok.Data;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;

@Data
@Builder
public class PagoPedidoResponse {
    private Integer idpago;
    private PedidoResponse pedido;
    private Double montopagado;
    private MetodoPagoResponse idmetodopago;
    private String estadopago;
}
