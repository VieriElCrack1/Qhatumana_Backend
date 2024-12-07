package pe.edu.cibertec.qhatumana.model.dto.request.pedido.detalle;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DetallePedidoUpdateRequest {
    private Integer iddetallepedido;
    private Integer idproducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
