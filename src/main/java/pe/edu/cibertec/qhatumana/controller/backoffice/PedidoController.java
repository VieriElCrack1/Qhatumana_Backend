package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoAndDetalleCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoAndDetalleUpdateRequest;
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

    @GetMapping("/valorid")
    public ResponseEntity<Integer> valorId() {
        return new ResponseEntity<>(pedidoService.obtenerMaximoIdPedido(), HttpStatus.OK);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<PedidoListResponse>> listaPedido() {
        List<PedidoListResponse> response = pedidoService.listaPedidos();
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<PedidoResponse>> registrarPedido(@RequestBody PedidoAndDetalleCreateRequest request) {
        ResponseAPI<PedidoResponse> response = pedidoService.registrarPedido(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }

    @PutMapping("/modificar")
    public ResponseEntity<ResponseAPI<PedidoResponse>> actualizarPedido(@RequestBody PedidoAndDetalleUpdateRequest request) {
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
            respuesta.put("message", "Hubo un error al buscar el pedido : " + e.getMessage());
            respuesta.put("status", "ERROR");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @GetMapping("/consultarpedido")
    public ResponseEntity<List<PedidoListResponse>> consultarPedidoXNomprod(@RequestParam("nomcliente") String nomcliente) {
        List<PedidoListResponse> response = pedidoService.consultarPedido(nomcliente);
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
