package pe.edu.cibertec.qhatumana.model.dto.request;

public record AuthCreateUserRequest (String nomuser, String apeuser,
                                     String username, String password,
                                     String dni) {

}
