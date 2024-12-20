package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.model.dto.response.UsuarioResponse;

public interface IUsuarioService {
    Usuario guardarUsuario(Usuario usuario);
    Usuario iniciarSesion(String username);
    UsuarioResponse buscarUsuario(int id);
}
