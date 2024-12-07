package pe.edu.cibertec.qhatumana.model.dto.response.cliente;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClienteResponse {
    private Integer idcliente;
    private String nomcliente;
    private String apecliente;
    private String dni;
    private String telefono;
    private String email;
    private Boolean estado;
}
