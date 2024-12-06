package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IPedidoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pedido")
public class PedidoController {

    private final IPedidoService pedidoService;

    @GetMapping("/lista")
    public ResponseAPI<List<PedidoListResponse>> listaPedido() {
        List<PedidoListResponse> response = pedidoService.listaPedidos();
        if(CollectionUtils.isEmpty(response)) {
            return ResponseAPI.<List<PedidoListResponse>>builder()
                    .message("No se encontro ningun pedido registrado")
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .data(response)
                    .build();
        }
        return ResponseAPI.<List<PedidoListResponse>>builder()
                .message("Pedidos Encontrados")
                .status("EXITO")
                .httpStatus(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<PedidoResponse>> registrarPedido(@RequestBody PedidoCreateRequest request) {
        ResponseAPI<PedidoResponse> response = pedidoService.registrarPedido(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }

    @PutMapping("/modificar")
    public ResponseEntity<ResponseAPI<PedidoResponse>> actualizarPedido(@RequestBody PedidoUpdateRequest request) {
        ResponseAPI<PedidoResponse> response = pedidoService.actualizarPedido(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }

    @GetMapping("/buscar/{idpedido}")
    public ResponseEntity<Map<String, Object>> buscarPedido(@PathVariable("idpedido") Integer idpedido) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            PedidoResponse response = pedidoService.buscarPedido(idpedido);
            respuesta.put("message", "Pedido Encontrado");
            respuesta.put("status", "EXITO");
            respuesta.put("data", response);
        }catch (DataAccessException e) {
            respuesta.put("message", "No se pudo acceder a la base de datos");
            respuesta.put("status", "ERROR");
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            respuesta.put("message", "Hubo un error al buscar el pedido");
            respuesta.put("status", "ERROR");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
