package pe.edu.cibertec.qhatumana.util.exception.handler;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
