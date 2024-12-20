package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.MetodoPagoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IMetodoPagoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/metodopago")
public class MetodoPagoController {

    private final IMetodoPagoService metodoPagoService;

    @GetMapping("/lista")
    public ResponseEntity<List<MetodoPagoResponse>> listadoMetodoPago() {
        List<MetodoPagoResponse> response = metodoPagoService.listadoMetodoPago();
        if(CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
