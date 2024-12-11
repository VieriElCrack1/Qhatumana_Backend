package pe.edu.cibertec.qhatumana.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.exception.MensajeExceptionResponse;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MensajeExceptionResponse resourceNotFoundException(ResourceNotFoundException ex,
                                                              WebRequest request) {
        return MensajeExceptionResponse.builder()
                .codigoEstado(HttpStatus.NOT_FOUND.value())
                .mensaje(ex.getMessage())
                .descripcion(request.getDescription(true))
                .fechaError(new Date())
                .build();
    }

}
