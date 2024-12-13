package pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura;

import lombok.Builder;
import lombok.Data;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;

import java.time.LocalDate;

@Data
@Builder
public class FacturaPedidoResponse {
    private Integer idfactura;
    private PedidoResponse pedido;
    private String numfactura;
    private LocalDate fechaemision;
    private Double montototal;
    private String estadofactura;
    private String urlfactura;
}
