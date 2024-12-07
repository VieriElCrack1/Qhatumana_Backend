package pe.edu.cibertec.qhatumana.model.dto.response.pedido.detalle;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DetallePedidoResponse {
    private Integer iddetallepedido;
    private Integer idproducto;
    private String nomprod;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
