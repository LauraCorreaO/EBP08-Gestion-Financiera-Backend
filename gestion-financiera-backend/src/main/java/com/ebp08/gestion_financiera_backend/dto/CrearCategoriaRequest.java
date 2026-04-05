package com.ebp08.gestion_financiera_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearCategoriaRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    private String descripcion;

    
}
