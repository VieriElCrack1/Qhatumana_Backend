package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.cliente.ClienteCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.cliente.ClienteUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.cliente.ClienteResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.service.interfaces.IClienteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    private final IClienteService clienteService;

    @GetMapping("/lista")
    public ResponseAPI<List<ClienteResponse>> listadoCliente() {
        List<ClienteResponse> listado = clienteService.listadoClientes();
        if (listado.isEmpty()) {
            return ResponseAPI.<List<ClienteResponse>>builder()
                    .message("No se encontro ningun cliente")
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .data(listado)
                    .build();
        }
        return ResponseAPI.<List<ClienteResponse>>builder()
                .message("Clientes Encontrados")
                .status("EXITO")
                .httpStatus(HttpStatus.OK.value())
                .data(listado)
                .build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<ClienteResponse>> registrarCliente(@RequestBody ClienteCreateRequest request) {
        ResponseAPI<ClienteResponse> response;
        try {
             response = clienteService.crearCliente(request);
        } catch (DataIntegrityViolationException e) { //excepcion para datos duplicados
            ResponseAPI<ClienteResponse> errorResponse = ResponseAPI.<ClienteResponse>builder()
                    .message("Ya existe un cliente con el mismo DNI o correo electrónico.")
                    .errorCode(HttpStatus.CONFLICT.name())
                    .status("ERROR")
                    .httpStatus(HttpStatus.CONFLICT.value())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ResponseAPI<ClienteResponse> errorReponse = ResponseAPI.<ClienteResponse>builder()
                    .message("No se pudo registrar el cliente")
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
            return new ResponseEntity<>(errorReponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<ResponseAPI<ClienteResponse>> actualizarCliente(@RequestBody ClienteUpdateRequest request) {
        ResponseAPI<ClienteResponse> response;
        try {
            response = clienteService.updateCliente(request);
        } catch (DataIntegrityViolationException e) { //excepcion para datos duplicados
            ResponseAPI<ClienteResponse> errorResponse = ResponseAPI.<ClienteResponse>builder()
                    .message("Ya existe un cliente con el mismo DNI o correo electrónico.")
                    .errorCode(HttpStatus.CONFLICT.name())
                    .status("ERROR")
                    .httpStatus(HttpStatus.CONFLICT.value())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ResponseAPI<ClienteResponse> errorReponse = ResponseAPI.<ClienteResponse>builder()
                    .message("No se pudo modificar el cliente")
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
            return new ResponseEntity<>(errorReponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/buscar/{idcliente}")
    public ResponseEntity<Map<String, Object>> buscarCliente(@PathVariable("idcliente") Integer idcliente) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            ClienteResponse response = clienteService.buscarCliente(idcliente);
            respuesta.put("message", "Cliente Encontrado");
            respuesta.put("status", "EXITO");
            respuesta.put("data", response);
        }catch (DataAccessException e) {
            respuesta.put("message", "No se pudo acceder a la base de datos");
            respuesta.put("status", "ERROR");
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            respuesta.put("message", "Hubo un error al buscar el cliente");
            respuesta.put("status", "ERROR");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
