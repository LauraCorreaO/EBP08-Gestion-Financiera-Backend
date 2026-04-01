package com.ebp08.gestion_financiera_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// ETIQUETAS: SPRING BOOT
// Para la DB
@Entity
@Table(name = "presupuesto")

// Encapsulación: Superútil con Lombok
@Data               // Getters y Setters
@NoArgsConstructor  // Obligatorio para JPA
@AllArgsConstructor

public class Presupuesto {

    // Para la DB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Para la DB
    @ManyToOne // MUCHOS presupuestos pertenecen a UN usuario
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Para la DB
    @ManyToOne // UN presupuesto puede pertenece a UNA categoria
    @JoinColumn(name = "id_categoria", nullable = true)
    private Categoria categoria;

    // Para la DB, cambio de nombre
    @Column(name = "monto_limite") // En DB es mejor snake_case
    private BigDecimal montoLimite;

    // Para la DB, cambio de nombre
    @Column(name = "fecha_limite") // En DB es mejor snake_case
    private LocalDateTime fechaLimite;
}
