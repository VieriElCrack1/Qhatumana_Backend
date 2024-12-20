package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago.PagoPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.pago.PagoPedidoUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoConsultaResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.PagoPedidoResponse;

import java.util.List;

public interface IPagoPedidoService {
    List<PagoPedidoResponse> listadoPagoPedido();
    ResponseAPI<PagoPedidoResponse> registrarPagoPedido(PagoPedidoRequest request);
    ResponseAPI<PagoPedidoResponse> actualizarPagoPedido(PagoPedidoUpdateRequest request);
    List<PagoPedidoConsultaResponse> consultarPagoPedidoXnomcliente(String cliente);
}