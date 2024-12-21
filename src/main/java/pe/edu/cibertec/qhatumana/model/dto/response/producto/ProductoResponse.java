package pe.edu.cibertec.qhatumana.model.dto.response.producto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductoResponse {
    private Integer idproducto;
    private String nomproducto;
    private String descrip;
    private Double precio;
    private Integer stock;
    private String urlproducto;
    private String nomcategoria;
    private Boolean estado;
}