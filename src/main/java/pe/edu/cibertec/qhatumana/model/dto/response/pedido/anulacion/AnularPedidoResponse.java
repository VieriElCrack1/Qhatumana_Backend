package pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AnularPedidoResponse {
    private Integer idanulacion;
    private Integer idpedido;
    private String motivoanulacion;
    private LocalDate fechaanulacion;
}
