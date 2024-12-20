package pe.edu.cibertec.qhatumana.model.dto.response.usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioPerfilResponse {
    private String message;
    private int status;
    private String messagedescrip;
}
