package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaUpdatePedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaConsultaResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaPedidoResponse;

import java.util.List;

public interface IFacturaService {
    List<FacturaPedidoResponse> listaFacturaPedidoResponse();
    ResponseAPI<FacturaPedidoResponse> guardarFacturaPedidoResponse(FacturaPedidoRequest facturaPedidoRequest);
    ResponseAPI<FacturaPedidoResponse> updateFacturaUpdatePedidoReques(FacturaUpdatePedidoRequest request);
    List<FacturaConsultaResponse> consultarFacturaXNomcliente(String cliente);
}
