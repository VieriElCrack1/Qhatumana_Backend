package pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacturaUpdatePedidoRequest {
    private Integer idfactura;
    private Integer estadofactura;
}
