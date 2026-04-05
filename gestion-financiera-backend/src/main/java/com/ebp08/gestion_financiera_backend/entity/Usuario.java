package com.ebp08.gestion_financiera_backend.entity;

import java.time.LocalDateTime;

import com.ebp08.gestion_financiera_backend.enums.Estado;
import jakarta.persistence.*;
import lombok.*;


// ETIQUETAS: SPRING BOOT
// Para la DB
@Entity
@Table(name = "usuario")

// Encapsulación: Super útil con Lombok
@Data               // Getters y Setters
@NoArgsConstructor  // Obligatorio para JPA
@AllArgsConstructor

public class Usuario {

    // Para la DB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String clave;

    // Para la DB
    @Column(name = "fecha_registro") // En DB es mejor snake_case
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING) // Para la DB,sirve para decirle a la base de datos como guardar el enum, en este caso en texto
    private Estado estado;

}
