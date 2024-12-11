package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.EnlaceMenu;
import pe.edu.cibertec.qhatumana.repository.EnlaceMenuRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IEnlaceMenuService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;

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
        return enlaceMenuRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontraron menus"));
    }

    @Override
    public List<EnlaceMenu> traerEnlaceUsuario(int codRol) {
        return enlaceMenuRepository.traerEnlaceUsuario(codRol);
    }
}
