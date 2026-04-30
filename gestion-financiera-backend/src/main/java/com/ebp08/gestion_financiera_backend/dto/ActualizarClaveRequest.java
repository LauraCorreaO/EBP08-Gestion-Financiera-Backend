package com.ebp08.gestion_financiera_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarClaveRequest {

    @NotBlank(message = "Debe ingresar la contraseña antigua.")
    private String claveAntigua;

    @NotBlank(message = "Debe ingresar la contraseña nueva.")
    private String claveNueva;

}
