package com.ebp08.gestion_financiera_backend.dto;

import com.ebp08.gestion_financiera_backend.enums.FrecuenciaTransaccion;
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CrearTransaccionProgramadaRequest {

    private String monto;         // String igual que CrearTransaccionRequest, se valida y convierte en el service
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;   // opcional, null = indefinida
    private FrecuenciaTransaccion frecuencia;
    private TipoTransaccion tipo;
    private Long idCategoria;
}