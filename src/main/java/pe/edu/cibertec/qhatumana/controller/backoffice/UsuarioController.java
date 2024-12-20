package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.qhatumana.model.dto.response.UsuarioResponse;
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
}
