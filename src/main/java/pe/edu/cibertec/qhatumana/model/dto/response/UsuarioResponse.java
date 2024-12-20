package pe.edu.cibertec.qhatumana.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private Integer idusuario;
    private String nomusuario;
    private String apeusuario;
    private String email;
    private String urlfoto;
    private String dni;
    private String estado;
}
