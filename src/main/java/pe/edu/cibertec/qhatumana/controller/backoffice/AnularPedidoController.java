package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.anulacion.AnularPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion.AnularPedidoConsultaResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion.AnularPedidoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IAnularPedidoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/anularpedido")
public class AnularPedidoController {

    private final IAnularPedidoService anularPedidoService;

    @GetMapping("/lista")
    public ResponseEntity<List<AnularPedidoResponse>> listadoPedidoResponse() {
        List<AnularPedidoResponse> response = anularPedidoService.listadoAnularPedido();
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<AnularPedidoResponse>> registrarAnulacionPedido(@RequestBody AnularPedidoRequest request) {
        ResponseAPI<AnularPedidoResponse> responseAPI = anularPedidoService.anularPedido(request);
        return new ResponseEntity<>(responseAPI, HttpStatusCode.valueOf(responseAPI.getHttpStatus()));
    }

    @GetMapping("/consultaranulacionpedido")
    public ResponseEntity<List<AnularPedidoConsultaResponse>> consultarAnulacionPedidoXCliente(@RequestParam("cliente") String cliente) {
        List<AnularPedidoConsultaResponse> response = anularPedidoService.consularAnulacionPedidoXCliente(cliente);
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
