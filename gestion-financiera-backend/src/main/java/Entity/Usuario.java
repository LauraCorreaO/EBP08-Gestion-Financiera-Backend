package Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuario") // Especifica el nombre de la tabla en la base de datos

// BUENAS PARACTICAS:
@Data // Genera getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor 
@AllArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que el ID se genere automáticamente
    private Long id;

    private String nombre;

    private String correo;

    private String contraseña;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING) // Almacena el valor del enum como una cadena en la base de datos
    private Estado estado;



}
