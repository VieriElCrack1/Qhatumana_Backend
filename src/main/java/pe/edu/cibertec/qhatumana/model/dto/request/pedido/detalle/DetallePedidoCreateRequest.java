package pe.edu.cibertec.qhatumana.model.dto.request.pedido.detalle;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DetallePedidoCreateRequest {
    private Integer idproducto;
    private Integer cantidad;
}
