package pe.edu.cibertec.qhatumana.model.dto.response.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseAPI<T> {
    private String status;
    private String message;
    private int httpStatus;
    private T data;
    private String errorCode;
    private String messageDescription;
}
