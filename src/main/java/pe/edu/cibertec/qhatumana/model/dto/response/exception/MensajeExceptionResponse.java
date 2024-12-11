package pe.edu.cibertec.qhatumana.model.dto.response.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MensajeExceptionResponse {
    private Integer codigoEstado;
    private Date fechaError;
    private String mensaje;
    private String descripcion;
}
