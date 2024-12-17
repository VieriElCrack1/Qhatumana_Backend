package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaUpdatePedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaPedidoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IFacturaService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/factura")
public class FacturaController {

    private final IFacturaService facturaService;

    @GetMapping("/lista")
    public ResponseEntity<List<FacturaPedidoResponse>> listadoFacturas() {
        List<FacturaPedidoResponse> facturas = facturaService.listaFacturaPedidoResponse();
        if(CollectionUtils.isEmpty(facturas)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseAPI<FacturaPedidoResponse>> registrarFactura(@RequestBody FacturaPedidoRequest request) {
        ResponseAPI<FacturaPedidoResponse> response = facturaService.guardarFacturaPedidoResponse(request);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getHttpStatus()));
    }

    @PutMapping("/modificar")
    public ResponseEntity<ResponseAPI<FacturaPedidoResponse>> actualizarFactura(@RequestBody FacturaUpdatePedidoRequest request) {
        ResponseAPI<FacturaPedidoResponse> response = facturaService.updateFacturaUpdatePedidoReques(request);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getHttpStatus()));
    }
}
