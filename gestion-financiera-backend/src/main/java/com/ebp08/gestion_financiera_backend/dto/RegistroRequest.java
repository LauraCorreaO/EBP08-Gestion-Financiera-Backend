package com.ebp08.gestion_financiera_backend.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombre;
    private String correo;
    private String clave;
}
