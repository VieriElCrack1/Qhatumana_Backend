package pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AnularPedidoConsultaResponse {
    private Integer idanulacion;
    private String cliente;
    private Double montototal;
    private String direccion;
    private Date fechaanulacion;
    private String nomestadopedido;
}
