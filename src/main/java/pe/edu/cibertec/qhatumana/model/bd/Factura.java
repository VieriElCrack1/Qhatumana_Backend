package pe.edu.cibertec.qhatumana.model.bd;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idfactura;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    private String numfactura;
    private String urlfactura;
    private String nomfactura;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaemision;
    private Double montototal;
    private Boolean estadofactura;
}
