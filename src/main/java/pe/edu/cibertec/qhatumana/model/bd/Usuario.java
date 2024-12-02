package pe.edu.cibertec.qhatumana.model.bd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idusuario;
    private String nomusuario;
    private String apeusuario;
    @Column(name = "emailusuario")
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "passusuario")
    private String password;
    @Column(name = "passrepusuario")
    private String reppassword;
    private String urlfoto;
    private String nomfoto;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechareg;
    @Column(length = 8, columnDefinition = "char(8)")
    private String dni;
    private Boolean estado;

    @JoinColumn(name = "idrol", nullable = false)
    @ManyToOne
    private Rol rol;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidoList;

}
