package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.cibertec.qhatumana.model.dto.request.usuario.UsuarioRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.UsuarioResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.usuario.UsuarioPerfilResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IUsuarioService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("id") int id) {
        try {
            UsuarioResponse usuarioResponse = usuarioService.buscarUsuario(id);
            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        }catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modificarconimagen")
    public ResponseEntity<UsuarioPerfilResponse> guardarUsuarioConImagen(@RequestParam("idusuario") Integer idusuario, @RequestParam("file") MultipartFile file) {
        UsuarioPerfilResponse response = usuarioService.actualizarUsuarioConImagen(idusuario,file);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    @PutMapping("/modificarsinimagen")
    public ResponseEntity<UsuarioPerfilResponse> guardarUsuarioSinImagen(@RequestBody UsuarioRequest request) {
        UsuarioPerfilResponse response = usuarioService.actualizarUsuarioSinImagen(request);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
