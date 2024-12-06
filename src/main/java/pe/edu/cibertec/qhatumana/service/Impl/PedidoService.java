package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.Cliente;
import pe.edu.cibertec.qhatumana.model.bd.EstadoPedido;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;
import pe.edu.cibertec.qhatumana.repository.PedidoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IPedidoService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PedidoService implements IPedidoService {

    private final PedidoRepository pedidoRepository;

    @Override
    public List<PedidoListResponse> listaPedidos() {
        return convertirPedidosListResponse(pedidoRepository.findAll());
    }

    @Override
    public ResponseAPI<PedidoResponse> registrarPedido(PedidoCreateRequest request) {
        Pedido guardado;
        try {
            Pedido pedido = new Pedido();
            //atributos
            Cliente cliente = new Cliente();
            cliente.setIdcliente(request.getIdcliente());
            pedido.setCliente(cliente);

            Usuario usuario = new Usuario();
            usuario.setIdusuario(request.getIdusuario());
            pedido.setUsuario(usuario);

            pedido.setFechareg(LocalDate.now());
            pedido.setIgv(0.18);
            pedido.setDescuento(request.getDescuento());
            pedido.setMontototal(request.getMontototal());
            pedido.setDireccion(request.getDireccion());

            EstadoPedido estadoPedido = new EstadoPedido();
            estadoPedido.setIdestado(request.getIdestado());
            pedido.setEstadoPedido(estadoPedido);

            guardado = pedidoRepository.save(pedido);
        }catch (DataAccessException e) {
          return ResponseAPI.<PedidoResponse>builder()
                  .message("Error hubo algun problema al acceder a la base de datos")
                  .status("ERROR")
                  .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                  .build();
        } catch (Exception e) {
            return ResponseAPI.<PedidoResponse>builder()
                    .message("Error no se pudo registrar el producto")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .build();
        }
        return ResponseAPI.<PedidoResponse>builder()
                .message("Pedido registrado exitosamente")
                .data(convertirPedidoResponse(guardado))
                .status("EXITO")
                .httpStatus(HttpStatus.CREATED.value())
                .build();
    }

    @Override
    public ResponseAPI<PedidoResponse> actualizarPedido(PedidoUpdateRequest request) {
        Pedido update;
        try{
            Pedido pedido = pedidoRepository.findById(request.getIdpedido()).orElseThrow(() -> new RuntimeException("No se encontro el pedido : " + request.getIdpedido()));

            Cliente cliente = new Cliente();
            cliente.setIdcliente(request.getIdcliente());
            pedido.setCliente(cliente);

            Usuario usuario = new Usuario();
            usuario.setIdusuario(request.getIdusuario());
            pedido.setUsuario(usuario);

            pedido.setDescuento(request.getDescuento());
            pedido.setMontototal(request.getMontototal());
            pedido.setDireccion(request.getDireccion());

            EstadoPedido estadoPedido = new EstadoPedido();
            estadoPedido.setIdestado(request.getIdestado());
            pedido.setEstadoPedido(estadoPedido);

            update = pedidoRepository.save(pedido);
        } catch (DataAccessException e) {
            return ResponseAPI.<PedidoResponse>builder()
                    .message("Error hubo algun problema al acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
        } catch (Exception e) {
            return ResponseAPI.<PedidoResponse>builder()
                    .message("Error No se pudo modificar el producto")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .build();
        }
        return ResponseAPI.<PedidoResponse>builder()
                .message("Pedido registrado exitosamente")
                .data(convertirPedidoResponse(update))
                .status("EXITO")
                .httpStatus(HttpStatus.OK.value())
                .build();
    }

    @Override
    public PedidoResponse buscarPedido(int idpedido) {
        return convertirPedidoResponse(pedidoRepository.findById(idpedido).orElseThrow(() -> new RuntimeException("No se encontro ningun pedido al id : " + idpedido)));
    }

    private List<PedidoListResponse> convertirPedidosListResponse(List<Pedido> pedidos) {
        List<PedidoListResponse> response = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            response.add(convertirPedidoListResponseBD(pedido));
        }
        return response;
    }

    private PedidoListResponse convertirPedidoListResponseBD(Pedido pedido) {
        return PedidoListResponse.builder()
                .idpedido(pedido.getIdpedido())
                .cliente(pedido.getCliente().getNomcliente() + " " + pedido.getCliente().getApecliente())
                .usuario(pedido.getUsuario().getNomusuario() + " " + pedido.getUsuario().getApeusuario())
                .descuento(pedido.getDescuento())
                .montototal(pedido.getMontototal())
                .direccion(pedido.getDireccion())
                .estado(pedido.getEstadoPedido().getNomestado())
                .build();
    }

    private PedidoResponse convertirPedidoResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .idpedido(pedido.getIdpedido())
                .idcliente(pedido.getCliente().getIdcliente())
                .idusuario(pedido.getUsuario().getIdusuario())
                .descuento(pedido.getDescuento())
                .montototal(pedido.getMontototal())
                .direccion(pedido.getDireccion())
                .idestado(pedido.getEstadoPedido().getIdestado())
                .build();
    }
}
