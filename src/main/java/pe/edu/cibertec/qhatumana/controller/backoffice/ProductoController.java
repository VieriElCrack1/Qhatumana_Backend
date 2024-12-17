package pe.edu.cibertec.qhatumana.controller.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.qhatumana.model.dto.response.producto.ProductoResponse;
import pe.edu.cibertec.qhatumana.service.interfaces.IProductoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/producto")
public class ProductoController {

    private final IProductoService productoService;

    //@PreAuthorize("hasRole('Cliente')")
    @GetMapping("/lista")
    public ResponseEntity<List<ProductoResponse>> listadoProductos() {
        List<ProductoResponse> response = productoService.listaProductoResponses();
        if(CollectionUtils.isEmpty(response)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
