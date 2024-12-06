package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;

import java.util.List;

public interface IPedidoService {
    List<PedidoListResponse> listaPedidos();
    ResponseAPI<PedidoResponse> registrarPedido(PedidoCreateRequest request);
    ResponseAPI<PedidoResponse> actualizarPedido(PedidoUpdateRequest request);
    PedidoResponse buscarPedido(int idpedido);
}
