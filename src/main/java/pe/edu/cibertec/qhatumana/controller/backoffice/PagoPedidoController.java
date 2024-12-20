package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago.PagoPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago.PagoPedidoUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoConsultaResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IPagoPedidoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pagopedido")
public class PagoPedidoController {

    private final IPagoPedidoService pagoPedidoService;

    @GetMapping("/lista")
    public ResponseEntity<List<PagoPedidoResponse>> listadoPagoPedido() {
        List<PagoPedidoResponse> response = pagoPedidoService.listadoPagoPedido();
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<PagoPedidoResponse>> pagoPedidoRegistrar(@RequestBody PagoPedidoRequest request) {
        ResponseAPI<PagoPedidoResponse> pagoPedidoResponse = pagoPedidoService.registrarPagoPedido(request);
        return new ResponseEntity<>(pagoPedidoResponse, HttpStatusCode.valueOf(pagoPedidoResponse.getHttpStatus()));
    }

    @PutMapping("/modificar")
    public ResponseEntity<ResponseAPI<PagoPedidoResponse>> actualizarPagoPedidoActualizar(@RequestBody PagoPedidoUpdateRequest request) {
        ResponseAPI<PagoPedidoResponse> pagoPedidoResponse = pagoPedidoService.actualizarPagoPedido(request);
        return new ResponseEntity<>(pagoPedidoResponse, HttpStatusCode.valueOf(pagoPedidoResponse.getHttpStatus()));
    }

    @GetMapping("/consultarpagopedido")
    public ResponseEntity<List<PagoPedidoConsultaResponse>> consultaPagoPedidoConsulta(@RequestParam("cliente") String cliente) {
        List<PagoPedidoConsultaResponse> response = pagoPedidoService.consultarPagoPedidoXnomcliente(cliente);
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
