package pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetodoPagoResponse {
    private Integer idmetodopago;
    private String nommetodopago;
}
