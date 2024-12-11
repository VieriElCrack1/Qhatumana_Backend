package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago.PagoPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IPagoPedidoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pagopedido")
public class PagoPedidoController {

    private final IPagoPedidoService pagoPedidoService;

    @GetMapping("/lista")
    public ResponseEntity<ResponseAPI<List<PagoPedidoResponse>>> listadoPagoPedido() {
        List<PagoPedidoResponse> response = pagoPedidoService.listadoPagoPedido();
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(ResponseAPI.<List<PagoPedidoResponse>>builder()
                    .message("No se encontraron listado de pago de pedidos")
                    .status("EXITO")
                    .data(response)
                    .httpStatus(HttpStatus.NO_CONTENT.value())
                    .errorCode(HttpStatus.NO_CONTENT.name())
                    .build(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ResponseAPI.<List<PagoPedidoResponse>>builder()
                .message("Se encontraron listados de pago de pedido")
                .status("EXITO")
                .data(response)
                .httpStatus(HttpStatus.OK.value())
                .errorCode(HttpStatus.OK.name())
                .build(), HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<PagoPedidoResponse>> pagoPedidoRegistrar(@RequestBody PagoPedidoRequest request) {
        ResponseAPI<PagoPedidoResponse> pagoPedidoResponse = pagoPedidoService.registrarPagoPedido(request);
        return new ResponseEntity<>(pagoPedidoResponse, HttpStatusCode.valueOf(pagoPedidoResponse.getHttpStatus()));
    }
}
