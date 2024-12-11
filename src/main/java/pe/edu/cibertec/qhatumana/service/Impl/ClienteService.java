package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.Cliente;
import pe.edu.cibertec.qhatumana.model.dto.request.cliente.ClienteCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.cliente.ClienteUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.cliente.ClienteResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.repository.ClienteRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IClienteService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteResponse> listadoClientes() {
        return convertirListClienteResponse(clienteRepository.findAll());
    }

    @Override
    public ResponseAPI<ClienteResponse> crearCliente(ClienteCreateRequest request) {
        Cliente e = new Cliente();
        e.setNomcliente(request.getNomcliente());
        e.setApecliente(request.getApecliente());
        e.setDni(request.getDni());
        e.setTelefono(request.getTelefono());
        e.setEmail(request.getEmail());
        e.setFechareg(LocalDate.now());
        e.setEstado(true);

        Cliente save = clienteRepository.save(e);

        return ResponseAPI.<ClienteResponse>builder()
                .message("Cliente registrado exitosamente")
                .status("EXITO")
                .data(convertirClienteResponse(save))
                .httpStatus(HttpStatus.CREATED.value())
                .build();
    }

    @Override
    public ResponseAPI<ClienteResponse> updateCliente(ClienteUpdateRequest request) {
        Cliente cliente = clienteRepository.findById(request.getIdcliente())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el cliente"));

        cliente.setNomcliente(request.getNomcliente());
        cliente.setApecliente(request.getApecliente());
        cliente.setDni(request.getDni());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setEstado(request.getEstado());

        Cliente update = clienteRepository.save(cliente);

        return ResponseAPI.<ClienteResponse>builder()
                .message("Cliente Modificado Exitosamente")
                .status("EXITO")
                .data(convertirClienteResponse(update))
                .httpStatus(HttpStatus.OK.value())
                .build();
    }

    @Override
    public ClienteResponse buscarCliente(int idcliente) {
        Cliente cliente = clienteRepository.findById(idcliente)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el cliente"));
        return convertirClienteResponse(cliente);
    }

    private ClienteResponse convertirClienteResponse(Cliente e) {
        return ClienteResponse.builder()
                .idcliente(e.getIdcliente())
                .nomcliente(e.getNomcliente())
                .apecliente(e.getApecliente())
                .dni(e.getDni())
                .telefono(e.getTelefono())
                .email(e.getEmail())
                .estado(e.getEstado())
                .build();
    }

    private List<ClienteResponse> convertirListClienteResponse(List<Cliente> clientes) {
        List<ClienteResponse> response = new ArrayList<>();
        for (Cliente cliente : clientes) {
            response.add(convertirClienteResponse(cliente));
        }
        return response;
    }
}
