package pe.edu.cibertec.qhatumana.model.dto.response.pedido;

import lombok.Builder;
import lombok.Data;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.detalle.DetallePedidoResponse;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class PedidoResponse {
    private Integer idpedido;
    private Integer idcliente;
    private String cliente;
    private Integer idusuario;
    private String usuario;
    private LocalDate fechareg;
    private Double igv;
    private Double descuento;
    private Double montototal;
    private String direccion;
    private Integer idestado;
    private String nomestado;
    private List<DetallePedidoResponse> detalles;
}
