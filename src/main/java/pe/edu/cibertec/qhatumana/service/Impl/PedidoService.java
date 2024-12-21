package pe.edu.cibertec.qhatumana.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.*;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoAndDetalleCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.PedidoAndDetalleUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.detalle.DetallePedidoCreateRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.detalle.DetallePedidoUpdateRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoListResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.detalle.DetallePedidoResponse;
import pe.edu.cibertec.qhatumana.repository.*;
import pe.edu.cibertec.qhatumana.service.interfaces.IPedidoService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PedidoService implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final FacturaRepository facturaRepository;

    @Override
    public List<PedidoListResponse> listaPedidos() {
        return convertirPedidosListResponse(pedidoRepository.findAll());
    }

    @Modifying
    @Transactional
    @Override
    public ResponseAPI<PedidoResponse> registrarPedido(PedidoAndDetalleCreateRequest request) {
        Pedido guardado;
        try {
            Pedido pedido = new Pedido();
            //atributos
            Cliente cliente = clienteRepository.findById(request.getIdcliente()).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            pedido.setCliente(cliente);

            Usuario usuario = usuarioRepository.findById(request.getIdusuario()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            pedido.setUsuario(usuario);

            pedido.setFechapedido(LocalDate.now());
            pedido.setIgv(0.18);

            double descuento = request.getDescuento() / 100;

            pedido.setDescuento(descuento);

            pedido.setDireccion(request.getDireccion());

            EstadoPedido estadoPedido = estadoPedidoRepository.findById(request.getIdestado()).orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun estado pedido"));
            pedido.setEstadoPedido(estadoPedido);

            List<DetallePedido> detallePedidos = new ArrayList<>();
            double calcularDescuento = 0.0;
            double formatearDescuento = request.getDescuento() / 100;
            for (DetallePedidoCreateRequest detalleRequest : request.getDetalles()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);

                Producto producto = productoRepository.findById(detalleRequest.getIdproducto()).orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto"));

                if(producto.getStock() < detalleRequest.getCantidad()) {
                    return ResponseAPI.<PedidoResponse>builder()
                            .message("El stock del producto esta agotado")
                            .status("ERROR")
                            .httpStatus(HttpStatus.BAD_REQUEST.value())
                            .errorCode(HttpStatus.BAD_REQUEST.name())
                            .build();
                }

                producto.setStock(producto.getStock() - detalleRequest.getCantidad());
                productoRepository.save(producto);

                detalle.setProducto(producto);
                detalle.setCantidad(detalleRequest.getCantidad());

                double precioUnitario = producto.getPrecio();
                detalle.setPrecio(precioUnitario);

                double subtotal = precioUnitario * detalleRequest.getCantidad(); // Calculamos el subtotal
                detalle.setSubtotal(subtotal);
                calcularDescuento += subtotal;
                detallePedidos.add(detalle);
            }

            double montoDescuento = calcularDescuento * formatearDescuento;
            double igvMontoDescuento = montoDescuento * 0.18;
            double montoTotal = (calcularDescuento - montoDescuento) + igvMontoDescuento;

            pedido.setMontototal(montoTotal);

            pedido.setDetallePedidoList(detallePedidos);

            guardado = pedidoRepository.save(pedido);

        } catch (DataAccessException e) {
          return ResponseAPI.<PedidoResponse>builder()
                  .message("Error hubo algun problema al acceder a la base de datos")
                  .status("ERROR")
                  .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                  .messageDescription(e.getMessage())
                  .build();
        } catch (Exception e) {
            return ResponseAPI.<PedidoResponse>builder()
                    .message("Error no se pudo registrar el producto")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
        return ResponseAPI.<PedidoResponse>builder()
                .message("Pedido registrado exitosamente")
                .data(convertirPedidoResponse(guardado))
                .status("EXITO")
                .httpStatus(HttpStatus.CREATED.value())
                .build();
    }

    @Modifying
    @Transactional
    @Override
    public ResponseAPI<PedidoResponse> actualizarPedido(PedidoAndDetalleUpdateRequest request) {
        Pedido update;
        try{
            Pedido pedido = pedidoRepository.findById(request.getIdpedido()).orElseThrow(() -> new ResourceNotFoundException("No se encontro el pedido : " + request.getIdpedido()));

            EstadoPedido estadoPedido = estadoPedidoRepository.findById(request.getIdestado()).orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun estado pedido"));

            if(pedido.getEstadoPedido().getNomestado().equalsIgnoreCase("Entregado") || pedido.getEstadoPedido().getIdestado() == 2) {
                return ResponseAPI.<PedidoResponse>builder()
                        .message("El pedido ya esta entregado, no se puede actualizar")
                        .data(convertirPedidoResponse(pedido))
                        .status("EXITO")
                        .httpStatus(HttpStatus.OK.value())
                        .build();
            }

            if(pedido.getEstadoPedido().getNomestado().equalsIgnoreCase("Anulado") || pedido.getEstadoPedido().getIdestado() == 3) {
                return ResponseAPI.<PedidoResponse>builder()
                        .message("El pedido esta anulado, no se puede actualizar")
                        .data(convertirPedidoResponse(pedido))
                        .status("EXITO")
                        .httpStatus(HttpStatus.OK.value())
                        .build();
            }

            Cliente cliente = clienteRepository.findById(request.getIdcliente()).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            pedido.setCliente(cliente);

            Usuario usuario = usuarioRepository.findById(request.getIdusuario()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            pedido.setUsuario(usuario);

            double descuento = request.getDescuento() / 100;

            pedido.setDescuento(descuento);
            pedido.setDireccion(request.getDireccion());


            pedido.setEstadoPedido(estadoPedido);

            List<DetallePedido> detallePedidos = new ArrayList<>();
            double calcularDescuento = 0.0;
            double formatearDescuento = request.getDescuento() / 100;

            for (DetallePedidoUpdateRequest detalleRequest : request.getDetalles()) {
                if(detalleRequest.getIddetallepedido() == null || detalleRequest.getIddetallepedido() == 0) {
                    return ResponseAPI.<PedidoResponse>builder()
                            .message("El valor idpedido es requerido")
                            .status("ERROR")
                            .httpStatus(HttpStatus.BAD_REQUEST.value())
                            .errorCode(HttpStatus.BAD_REQUEST.name())
                            .build();
                }

                DetallePedido detalle = detallePedidoRepository.findById(detalleRequest.getIddetallepedido()).orElseThrow(() -> new ResourceNotFoundException("No se encontro ningún detalle de pedido registado"));
                detalle.setPedido(pedido);

                Producto producto = productoRepository.findById(detalleRequest.getIdproducto()).orElseThrow(() -> new RuntimeException("No se encontró el producto"));

                if(producto.getStock() < detalleRequest.getCantidad()) {
                    return ResponseAPI.<PedidoResponse>builder()
                            .message("El stock del producto esta agotado")
                            .status("ERROR")
                            .httpStatus(HttpStatus.BAD_REQUEST.value())
                            .errorCode(HttpStatus.BAD_REQUEST.name())
                            .build();
                }

                producto.setStock(producto.getStock() - detalleRequest.getCantidad());
                productoRepository.save(producto);

                detalle.setProducto(producto);
                detalle.setCantidad(detalleRequest.getCantidad());

                double precioUnitario = producto.getPrecio();
                detalle.setPrecio(precioUnitario);

                double subtotal = precioUnitario * detalleRequest.getCantidad(); // Calculamos el subtotal
                detalle.setSubtotal(subtotal);
                calcularDescuento += subtotal;
                detallePedidos.add(detalle);
            }

            double montoDescuento = calcularDescuento * formatearDescuento;
            double igvMontoDescuento = montoDescuento * 0.18;
            double montoTotal = (calcularDescuento - montoDescuento) + igvMontoDescuento;

            pedido.setMontototal(montoTotal);

            pedido.setDetallePedidoList(detallePedidos);

            update = pedidoRepository.save(pedido);
        } catch (DataAccessException e) {
            return ResponseAPI.<PedidoResponse>builder()
                    .message("Error hubo algun problema al acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .messageDescription(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseAPI.<PedidoResponse>builder()
                    .message("Error No se pudo modificar el producto")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
        return ResponseAPI.<PedidoResponse>builder()
                .message("Pedido modificado exitosamente")
                .data(convertirPedidoResponse(update))
                .status("EXITO")
                .httpStatus(HttpStatus.OK.value())
                .build();
    }

    @Override
    public PedidoResponse buscarPedido(int idpedido) {
        return convertirPedidoResponse(pedidoRepository.findById(idpedido).orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido al id : " + idpedido)));
    }

    @Override
    public int obtenerMaximoIdPedido() {
        return pedidoRepository.obtenerMaximoIdPedido();
    }

    @Override
    public List<PedidoListResponse> consultarPedido(String nomcliente) {
        return pedidoRepository.consultarPedido(nomcliente);
    }

    @Override
    public List<PedidoListResponse> consultarPedidoRegister(String cliente, Integer idestado) {
        return pedidoRepository.consultarPedidoRegister(cliente, idestado);
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
                .cliente(pedido.getCliente().getNomcliente() + " " + pedido.getCliente().getApecliente())
                .idusuario(pedido.getUsuario().getIdusuario())
                .usuario(pedido.getUsuario().getNomusuario() + " " + pedido.getUsuario().getApeusuario())
                .fechareg(pedido.getFechapedido())
                .igv(pedido.getIgv())
                .descuento(pedido.getDescuento())
                .montototal(pedido.getMontototal())
                .direccion(pedido.getDireccion())
                .idestado(pedido.getEstadoPedido().getIdestado())
                .nomestado(pedido.getEstadoPedido().getNomestado())
                .detalles(convertirDetallePedidoListResponse(pedido.getDetallePedidoList()))
                .build();
    }

    private List<DetallePedidoResponse> convertirDetallePedidoListResponse(List<DetallePedido> detallePedidos) {
        List<DetallePedidoResponse> response = new ArrayList<>();
        for (DetallePedido detallePedido : detallePedidos) {
            response.add(convertirDetallePedidoResponse(detallePedido));
        }
        return response;
    }

    private DetallePedidoResponse convertirDetallePedidoResponse(DetallePedido detallePedido) {
        return DetallePedidoResponse.builder()
                .iddetallepedido(detallePedido.getIdpedidodetalle())
                .idproducto(detallePedido.getProducto().getIdproducto())
                .nomprod(detallePedido.getProducto().getNomproducto())
                .cantidad(detallePedido.getCantidad())
                .precioUnitario(detallePedido.getPrecio())
                .subtotal(detallePedido.getSubtotal())
                .build();
    }
}
