package pe.edu.cibertec.qhatumana.model.dto.request.pedido;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PedidoCreateRequest {
    private Integer idcliente;
    private Integer idusuario;
    private Double descuento;
    private Double montototal;
    private String direccion;
    private Integer idestado;
}