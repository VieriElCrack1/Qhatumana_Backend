package pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagoPedidoConsultaResponse {
    private Integer idpago;
    private String cliente;
    private Double montopagado;
    private String metodopago;
    private String estadopago;
}
