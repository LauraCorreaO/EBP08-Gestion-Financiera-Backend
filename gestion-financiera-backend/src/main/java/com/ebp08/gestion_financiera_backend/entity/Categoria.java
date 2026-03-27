package com.ebp08.gestion_financiera_backend.entity;

import com.ebp08.gestion_financiera_backend.enums.TipoCategoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ETIQUETAS: SPRING BOOT
// Para la DB
@Entity
@Table(name = "categoria")

// Encapsulación: Superútil con Lombok
@Data               // Getters y Setters
@NoArgsConstructor  // Obligatorio para JPA
@AllArgsConstructor

public class Categoria {

    // Para la DB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    // Para la DB
    @ManyToOne // MUCHAS categorías pertenecen a UN usuario
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING) // Para la DB
    private TipoCategoria tipo;
}
