package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.dto.response.producto.ProductoResponse;
import pe.edu.cibertec.qhatumana.repository.ProductoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IProductoService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<ProductoResponse> listaProductoResponses() {
        return productoRepository.listadoProductos();
    }
}
