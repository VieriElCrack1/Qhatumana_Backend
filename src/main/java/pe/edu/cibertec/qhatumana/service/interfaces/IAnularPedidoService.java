package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.request.pedido.anulacion.AnularPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.anulacion.AnularPedidoResponse;

import java.util.List;

public interface IAnularPedidoService {
    List<AnularPedidoResponse> listadoAnularPedido();
    ResponseAPI<AnularPedidoResponse> anularPedido(AnularPedidoRequest request);
}
