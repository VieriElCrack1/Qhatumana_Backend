package pe.edu.cibertec.qhatumana.model.dto.response.pedido;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PedidoResponse {
    private Integer idpedido;
    private Integer idcliente;
    private Integer idusuario;
    private Double descuento;
    private Double montototal;
    private String direccion;
    private Integer idestado;
}
