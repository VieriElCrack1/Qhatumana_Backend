package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoAndDetalleCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoAndDetalleUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;

import java.util.List;

public interface IPedidoService {
    List<PedidoListResponse> listaPedidos();
    ResponseAPI<PedidoResponse> registrarPedido(PedidoAndDetalleCreateRequest request);
    ResponseAPI<PedidoResponse> actualizarPedido(PedidoAndDetalleUpdateRequest request);
    PedidoResponse buscarPedido(int idpedido);
}
