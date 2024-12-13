package pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FacturaPedidoRequest {
    private Integer idpedido;
}
