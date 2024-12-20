package pe.edu.cibertec.qhatumana.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("Select u from Usuario u where u.username=?1")
    Usuario iniciarSesion(String username);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario set urlfoto=:urlfoto, nomfoto=:nomfoto " +
            " WHERE idusuario=:idusuario")
    void actualizarUsuarioConImagen(String urlfoto, String nomfoto, Integer idusuario);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario set nomusuario=:nombre, apeusuario=:apeusuario, username=:email, password=:password, " +
            " reppassword=:reppassword, dni=:dni " +
            " WHERE idusuario=:idusuario")
    void actualizarUsuarioSinImagen(String nombre, String apeusuario, String email, String password, String reppassword,
                                    String dni, Integer idusuario);

    /*@Query("SELECT u.idusuario FROM Usuario u WHERE u.username=:nombre")
    int obtenerIdUsuarioxNombre(String nombre);*/
}
