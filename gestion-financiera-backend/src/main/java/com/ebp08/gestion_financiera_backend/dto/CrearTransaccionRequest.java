package com.ebp08.gestion_financiera_backend.dto;
import lombok.Data;
    
@Data

public class CrearTransaccionRequest {

    private Long idUsuario;
    private Long idCategoria;
    private String descripcion;
    private String monto; // Se recibe como String para evitar problemas de formato en la petición, luego se convierte a BigDecimal en el servicio.
}
