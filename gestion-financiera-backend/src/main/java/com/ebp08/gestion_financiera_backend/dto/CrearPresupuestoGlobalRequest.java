package com.ebp08.gestion_financiera_backend.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearPresupuestoGlobalRequest {

    private Long idUsuario;
    private BigDecimal montoLimite;


}
