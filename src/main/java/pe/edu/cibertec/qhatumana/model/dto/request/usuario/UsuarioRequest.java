package pe.edu.cibertec.qhatumana.model.dto.request.usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRequest {
    private Integer idusuario;
    private String nombre;
    private String apeusuario;
    private String email;
    private String password;
    private String dni;
}
