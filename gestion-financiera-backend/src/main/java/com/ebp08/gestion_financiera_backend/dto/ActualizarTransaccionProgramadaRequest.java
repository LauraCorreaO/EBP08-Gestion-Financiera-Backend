package com.ebp08.gestion_financiera_backend.dto;

import com.ebp08.gestion_financiera_backend.enums.Estado;
import com.ebp08.gestion_financiera_backend.enums.FrecuenciaTransaccion;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActualizarTransaccionProgramadaRequest {

    private String monto;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private FrecuenciaTransaccion frecuencia;
    private Estado estado;         // permite activar/pausar sin eliminar

    // No se permite cambiar tipo ni idCategoria:
    // cambiar el tipo (INGRESO <-> EGRESO) en una programación existente
    // es semánticamente incorrecto y rompería la coherencia del historial futuro.
    // Si el usuario necesita otro tipo, debe crear una nueva programación.
}