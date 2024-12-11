package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.response.producto.ProductoResponse;

import java.util.List;

public interface IProductoService {
    List<ProductoResponse> listaProductoResponses();
}
