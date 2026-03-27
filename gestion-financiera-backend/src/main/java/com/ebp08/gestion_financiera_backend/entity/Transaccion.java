package com.ebp08.gestion_financiera_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ETIQUETAS: SPRING BOOT
// para la DB
@Entity
@Table(name = "transaccion")

// Encapsulación: Superútil con Lombok
@Data               // Getters y Setters
@NoArgsConstructor  // Obligatorio para JPA
@AllArgsConstructor

public class Transaccion {

    // Etiquetas para la DB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Etiquetas para la DB
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Etiquetas para la DB
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    private BigDecimal monto; // Recordar que Ingres/Egreso se determina con la categoría
    private LocalDateTime fecha;
    private String descripcion;

}
