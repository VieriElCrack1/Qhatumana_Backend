package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.bd.Usuario;

public interface IUsuarioService {
    Usuario guardarUsuario(Usuario usuario);
    Usuario iniciarSesion(String username);
}
