package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("Select u from Usuario u where u.username=?1")
    Usuario iniciarSesion(String username);

    /*@Modifying
    @Transactional
    @Query("UPDATE Usuario set nombre=:nombre, username=:username, password=:password, " +
            "rep_pass=:rep_pass, urlfoto=:urlfoto, nombrefoto=:nombrefoto, dni=:dni, " +
            "idrol=:idrol, activo=:activo WHERE idusuario=:idusuario")
    void actualizarUsuario(String nombre, String username, String password, String rep_pass,
                           String urlfoto, String nombrefoto, String dni, Integer idrol,
                           Boolean activo, Integer idusuario);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario set nombre=:nombre, username=:username, password=:password, " +
            "rep_pass=:rep_pass, dni=:dni, idrol=:idrol, activo=:activo " +
            "WHERE idusuario=:idusuario")
    void actualizarUsuarioSinImagen(String nombre, String username, String password, String rep_pass,
                                    String dni, Integer idrol, Boolean activo, Integer idusuario);

    @Query("SELECT u.idusuario FROM Usuario u WHERE u.username=:nombre")
    int obtenerIdUsuarioxNombre(String nombre);*/
}
