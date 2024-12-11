package pe.edu.cibertec.qhatumana.service.interfaces;

import pe.edu.cibertec.qhatumana.model.bd.EnlaceMenu;

import java.util.List;

public interface IEnlaceMenuService {
    List<EnlaceMenu> listadoEnlaceMenus();
    EnlaceMenu buscarEnlaceMenu(int id);
    List<EnlaceMenu> traerEnlaceUsuario(int codRol);
}
