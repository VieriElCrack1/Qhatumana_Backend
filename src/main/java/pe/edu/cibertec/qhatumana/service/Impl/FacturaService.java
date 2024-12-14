package pe.edu.cibertec.qhatumana.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.DetallePedido;
import pe.edu.cibertec.qhatumana.model.bd.EstadoPedido;
import pe.edu.cibertec.qhatumana.model.bd.Factura;
import pe.edu.cibertec.qhatumana.model.bd.Pedido;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaPedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.pedido.factura.FacturaUpdatePedidoRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.api.ResponseAPI;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.PedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.detalle.DetallePedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaPedidoResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.factura.FacturaReportListResponse;
import pe.edu.cibertec.qhatumana.repository.FacturaRepository;
import pe.edu.cibertec.qhatumana.repository.PedidoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IFacturaService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;
import pe.edu.cibertec.qhatumana.util.firebase.storage.ArchivoService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FacturaService implements IFacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final ArchivoService archivoService;

    @Override
    public List<FacturaPedidoResponse> listaFacturaPedidoResponse() {
        return convertirListFacturaPedidoResponse(facturaRepository.findAll());
    }

    @Modifying
    @Transactional
    @Override
    public ResponseAPI<FacturaPedidoResponse> guardarFacturaPedidoResponse(FacturaPedidoRequest facturaPedidoRequest) {
        try {
            Factura factura = new Factura();

            Pedido pedido = pedidoRepository.findById(facturaPedidoRequest.getIdpedido()).orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido"));
            if (facturaRepository.existsByPedidoAndEstadofactura(pedido, true)) {
                throw new IllegalArgumentException("Este pedido ya tiene una factura generada.");
            }
            factura.setPedido(pedido);

            String numfactura = generarNumeroFactura();
            factura.setNumfactura(numfactura);

            factura.setFechaemision(LocalDate.now());
            factura.setMontototal(pedido.getMontototal());
            factura.setEstadofactura(true);

            EstadoPedido estadoPedido = new EstadoPedido();
            estadoPedido.setIdestado(2);
            pedido.setEstadoPedido(estadoPedido);

            pedidoRepository.save(pedido);

            Factura facturaSave = facturaRepository.save(factura);

            List<FacturaReportListResponse> response = facturaRepository.consultarFacturaReportedPedido(pedido.getIdpedido());
            InputStream jasperStream = getClass().getResourceAsStream("/facturapedido.jasper");
            if (jasperStream == null) {
                throw new ResourceNotFoundException("El archivo facturapedido.jasper no se encontro.");
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(response);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            File archivoPdf = File.createTempFile("tempPdf", ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(archivoPdf));

            String nombreArchivo = "factura_" + numfactura + ".pdf";
            String urlpdf = archivoService.uploadFile(archivoPdf, nombreArchivo, "application/pdf", "facturas");

            factura.setNomfactura(nombreArchivo);
            factura.setUrlfactura(urlpdf);
            facturaRepository.save(factura);

            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Factura registrada exitosamente")
                    .data(convertirFacturaPedidoResponse(facturaSave))
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .build();
        }catch (DataAccessException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("No se pudo acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (NullPointerException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Los valores de la factura son nulos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message(e.getMessage())
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Hubo un error al registrar la factura")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
    }

    @Modifying
    @Override
    public ResponseAPI<FacturaPedidoResponse> updateFacturaUpdatePedidoReques(FacturaUpdatePedidoRequest request) {
        try {
            Factura factura = facturaRepository.findById(request.getIdfactura()).orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));

            if (request.getEstadofactura() != 0 && request.getEstadofactura() != 1) {
                throw new IllegalArgumentException("Solo se permite cambiar el estado de la factua a facturada(1) o anulada(0)");
            }

            factura.setEstadofactura(request.getEstadofactura() == 0 ? false : true);

            Factura facturaSave = facturaRepository.save(factura);

            if (facturaSave.getEstadofactura() == false) {
                Pedido pedido = pedidoRepository.findById(facturaSave.getPedido().getIdpedido()).orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido para modificar"));
                EstadoPedido estadoPedido = new EstadoPedido();
                estadoPedido.setIdestado(1);
                pedido.setEstadoPedido(estadoPedido);

                pedidoRepository.save(pedido);
            }

            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Factura modificada exitosamente")
                    .data(convertirFacturaPedidoResponse(facturaSave))
                    .status("EXITO")
                    .httpStatus(HttpStatus.OK.value())
                    .build();
        }catch (DataAccessException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("No se pudo acceder a la base de datos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (IllegalArgumentException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message(e.getMessage())
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Los valores de la factura son nulos")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }catch (Exception e) {
            return ResponseAPI.<FacturaPedidoResponse>builder()
                    .message("Hubo un error al registrar la factura")
                    .status("ERROR")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .messageDescription(e.getMessage())
                    .build();
        }
    }

    private List<FacturaPedidoResponse> convertirListFacturaPedidoResponse(List<Factura> facturas) {
        List<FacturaPedidoResponse> response = new ArrayList<>();
        for (Factura factura : facturas) {
            response.add(convertirFacturaPedidoResponse(factura));
        }
        return response;
    }

    private FacturaPedidoResponse convertirFacturaPedidoResponse(Factura factura) {
        return FacturaPedidoResponse.builder()
                .idfactura(factura.getIdfactura())
                .pedido(convertirPedidoResponse(factura.getPedido()))
                .numfactura(factura.getNumfactura())
                .fechaemision(factura.getFechaemision())
                .montototal(factura.getMontototal())
                .estadofactura(factura.getEstadofactura() ? "Emitida" : "Anulada")
                .urlfactura(factura.getUrlfactura())
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

    private String generarNumeroFactura() {
        String numfactura = facturaRepository.obtenerUltimaPosicionNumFactura();

        if(numfactura == null) { //si no existe
            return "N000001";
        }

        int ultimoNumero = Integer.parseInt(numfactura.substring(1)); //resultado es 000001 sin la n
        int siguienteNumero = ultimoNumero + 1;
        return String.format("N%06d", siguienteNumero);
    }
}