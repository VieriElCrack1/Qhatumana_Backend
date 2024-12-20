package pe.edu.cibertec.qhatumana.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.model.dto.request.usuario.UsuarioRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.UsuarioResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.usuario.UsuarioPerfilResponse;

public interface IUsuarioService {
    Usuario guardarUsuario(Usuario usuario);
    Usuario iniciarSesion(String username);
    UsuarioResponse buscarUsuario(int id);
    UsuarioPerfilResponse actualizarUsuarioConImagen(Integer idusuario, MultipartFile file);
    UsuarioPerfilResponse actualizarUsuarioSinImagen(UsuarioRequest request);
}
