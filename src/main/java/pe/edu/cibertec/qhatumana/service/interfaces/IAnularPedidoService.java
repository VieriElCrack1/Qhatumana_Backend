package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;

public interface IAnularPedidoService {
    ResponseAPI<Void> anularPedido(int idpedido);
}
