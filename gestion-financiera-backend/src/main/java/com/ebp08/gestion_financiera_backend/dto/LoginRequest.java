package com.ebp08.gestion_financiera_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String clave;
}
