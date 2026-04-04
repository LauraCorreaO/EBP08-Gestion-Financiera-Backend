package com.ebp08.gestion_financiera_backend.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearPresupuestoCategoriaRequest {

    private Long idUsuario;
    private Long idCategoria;   
    private BigDecimal montoLimite;

}
