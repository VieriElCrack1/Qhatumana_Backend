package pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FacturaConsultaResponse {
    private Integer idfactura;
    private String cliente;
    private String emailcliente;
    private String direccionpedido;
    private Date fechaemision;
    private Double montotal;
    private String nompago;
    private String urlfactura;
    private Boolean estadofactura;
}
