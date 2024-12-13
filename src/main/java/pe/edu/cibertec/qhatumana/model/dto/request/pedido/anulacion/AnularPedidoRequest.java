package pe.edu.cibertec.qhatumana.model.dto.request.pedido.anulacion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnularPedidoRequest {
    private Integer idpedido;
    private String motivoanulacion;
}
