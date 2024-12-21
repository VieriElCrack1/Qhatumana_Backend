package pe.edu.cibertec.qhatumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.qhatumana.model.bd.Producto;
import pe.edu.cibertec.qhatumana.model.dto.response.producto.ProductoResponse;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT new pe.edu.cibertec.qhatumana.model.dto.response.producto.ProductoResponse(p.idproducto,p.nomproducto,p.descrip,p.precio,p.stock,p.urlproducto,c.nomcategoria,p.estado) " +
            " FROM Producto p" +
            " JOIN p.categoria c")
    List<ProductoResponse> listadoProductos();
}
