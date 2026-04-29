package com.ebp08.gestion_financiera_backend.entity;

import com.ebp08.gestion_financiera_backend.enums.Estado;
import com.ebp08.gestion_financiera_backend.enums.FrecuenciaTransaccion;
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion_programada")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionProgramada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    @Enumerated(EnumType.STRING)
    private FrecuenciaTransaccion frecuencia;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private BigDecimal monto;
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    /* ------------------>   PARTE SIMÓNNNN
     * Preparado para el proceso automático con @Scheduled (sprint futuro).
     * Por ahora siempre se inicializa en null al crear.
     * El job de scheduling lo actualizará cada vez que ejecute la transacción.
     */
    @Column(name = "ultima_ejecucion")
    private LocalDateTime ultimaEjecucion;
}