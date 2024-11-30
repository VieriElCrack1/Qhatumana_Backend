package pe.edu.cibertec.qhatumana;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.service.interfaces.IUsuarioService;

import java.time.LocalDate;

@SpringBootTest
class QhatumanaApplicationTests {

	@Autowired
	private IUsuarioService usuarioService;



	@Test
	void contextLoads() {
		try {
			Usuario u = new Usuario();
			u.setIdusuario(1);
			u.setNomusuario("Vieri Rober");
			u.setApeusuario("Santos Crispin");
			u.setUsername("vierisantos@gmail.com");
			u.setPassword("vieri123");
			String pass = u.getPassword();
			u.setReppassword(pass);
			u.setUrlfoto("vacio");
			u.setNomfoto("vacio");
			u.setFechareg(LocalDate.now());
			u.setDni("82818281");
			//u.setIdrol(1);
			u.setEstado(true);
			usuarioService.guardarUsuario(u);
		}catch (Exception e) {
			throw new RuntimeException("Error en : " + e.getMessage());
		}
	}

	@Test
	void consultarUsuarioExitse() {
		Usuario u = usuarioService.iniciarSesion("vierisantos@gmail.com");
		System.out.println("Nombre: " + u.getNomusuario());
	}

}
