package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.dto.request.cliente.ClienteCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.cliente.ClienteUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.cliente.ClienteResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;

import java.util.List;

public interface IClienteService {
    List<ClienteResponse> listadoClientes();
    ResponseAPI<ClienteResponse> crearCliente(ClienteCreateRequest request);
    ResponseAPI<ClienteResponse> updateCliente(ClienteUpdateRequest request);
    ClienteResponse buscarCliente(int idcliente);
}
