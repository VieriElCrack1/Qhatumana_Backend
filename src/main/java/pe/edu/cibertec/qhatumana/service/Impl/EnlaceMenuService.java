package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.EnlaceMenu;
import pe.edu.cibertec.qhatumana.repository.EnlaceMenuRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IEnlaceMenuService;

import java.util.List;

@AllArgsConstructor
@Service
public class EnlaceMenuService implements IEnlaceMenuService {
    private EnlaceMenuRepository enlaceMenuRepository;

    @Override
    public List<EnlaceMenu> listadoEnlaceMenus() {
        return enlaceMenuRepository.findAll();
    }

    @Override
    public EnlaceMenu buscarEnlaceMenu(int id) {
        return enlaceMenuRepository.findById(id).orElse(null);
    }

    @Override
    public int obtenerIdEnlaceMenu() {
        return enlaceMenuRepository.obtenerIdEnlaceMenu();
    }

    @Override
    public List<EnlaceMenu> traerEnlaceUsuario(int codRol) {
        return enlaceMenuRepository.traerEnlaceUsuario(codRol);
    }
}
