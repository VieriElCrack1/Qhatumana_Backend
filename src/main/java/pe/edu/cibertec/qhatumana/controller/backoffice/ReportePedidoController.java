package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IPedidoService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reporte")
public class ReportePedidoController {

    private final IPedidoService pedidoService;

    @GetMapping("/reportedia")
    public ResponseEntity<List<PedidoListResponse>> reporteDiario() {
        List<PedidoListResponse> response = pedidoService.reportePedidoDiario();
        if (CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reportesemanal")
    public ResponseEntity<List<PedidoListResponse>> reporteSemanal(@RequestParam("dia") int dia,
                                                                   @RequestParam("fechainicio") LocalDate fechainicio,
                                                                   @RequestParam("fechafin") LocalDate fechafin) {
        List<PedidoListResponse> response = pedidoService.reportePedidoSemanal(dia, fechainicio, fechafin);
        if (CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reportemensual")
    public ResponseEntity<List<PedidoListResponse>> reporteMensual(@RequestParam("mes") int mes) {
        List<PedidoListResponse> response = pedidoService.reportePedidoMensual(mes);
        if (CollectionUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
