package pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FacturaReportListResponse {
    private String cliente;
    private String emailcliente;
    private String direccionpedido;
    private Date fechaemision;
    private String numfactura;
    private String nomestadopedido;
    private String nomproducto;
    private String descripproducto;
    private String nomcategoria;
    private Integer cantidad;
    private Double precio;
    private Double subtotal;
    private Double montotal;
    private Double descuento;
    private String usuario;
    private String emailusuario;
    private String nompago;
}
