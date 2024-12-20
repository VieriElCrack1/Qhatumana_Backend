package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.MetodoPago;
import pe.edu.cibertec.qhatumana.model.dto.response.pedido.pago.MetodoPagoResponse;
import pe.edu.cibertec.qhatumana.repository.MetodoPagoRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IMetodoPagoService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MetodoPagoService implements IMetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    @Override
    public List<MetodoPagoResponse> listadoMetodoPago() {
        return convertirListMetodoPagoResponse(metodoPagoRepository.findAll());
    }

    private List<MetodoPagoResponse> convertirListMetodoPagoResponse(List<MetodoPago> metodoPagos) {
        List<MetodoPagoResponse> response = new ArrayList<>();
        for (MetodoPago metodoPago : metodoPagos) {
            response.add(convertirMetodoPago(metodoPago));
        }
        return response;
    }

    private MetodoPagoResponse convertirMetodoPago(MetodoPago metodoPago) {
        return MetodoPagoResponse.builder()
                .idmetodopago(metodoPago.getIdmetodopago())
                .nommetodopago(metodoPago.getNompago())
                .build();
    }
}
