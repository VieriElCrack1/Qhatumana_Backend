package pe.edu.cibertec.qhatumana.model.dto.request.cliente;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClienteCreateRequest {
    private String nomcliente;
    private String apecliente;
    private String dni;
    private String telefono;
    private String email;
}
