package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;

public interface IAnularPedido {
    ResponseAPI<Void> anularPedido(int idpedido);
}
