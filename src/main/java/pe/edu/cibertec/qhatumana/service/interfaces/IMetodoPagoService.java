package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.MetodoPagoResponse;

import java.util.List;

public interface IMetodoPagoService {
    List<MetodoPagoResponse> listadoMetodoPago();
}
