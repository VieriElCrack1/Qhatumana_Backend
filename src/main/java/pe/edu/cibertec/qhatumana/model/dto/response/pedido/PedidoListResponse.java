package pe.edu.cibertec.qhatumana.model.dto.response.pedido;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PedidoListResponse {
    private Integer idpedido;
    private String cliente;
    private String usuario;
    private Double descuento;
    private Double montototal;
    private String direccion;
    private String estado;
}
