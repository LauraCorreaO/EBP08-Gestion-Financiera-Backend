error id: file:///C:/Users/LauraCorreaOchoa/Desktop/Laura/2026-1/EBP08-Gestion-Financiera-Backend/gestion-financiera-backend/src/main/java/com/ebp08/gestion_financiera_backend/controller/UsuarioController.java:com/ebp08/gestion_financiera_backend/entity/Usuario#
file:///C:/Users/LauraCorreaOchoa/Desktop/Laura/2026-1/EBP08-Gestion-Financiera-Backend/gestion-financiera-backend/src/main/java/com/ebp08/gestion_financiera_backend/controller/UsuarioController.java
empty definition using pc, found symbol in pc: com/ebp08/gestion_financiera_backend/entity/Usuario#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 370
uri: file:///C:/Users/LauraCorreaOchoa/Desktop/Laura/2026-1/EBP08-Gestion-Financiera-Backend/gestion-financiera-backend/src/main/java/com/ebp08/gestion_financiera_backend/controller/UsuarioController.java
text:
```scala
package com.ebp08.gestion_financiera_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebp08.gestion_financiera_backend.dto.LoginRequest;
import com.ebp08.gestion_financiera_backend.dto.RegistroRequest;
import com.ebp08.gestion_financiera_backend.entity.@@Usuario;
import com.ebp08.gestion_financiera_backend.service.UsuarioService;

import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody RegistroRequest registroRequest) {
        Usuario creado = usuarioService.crearUsuario(registroRequest);
        return ResponseEntity.status(201).body(creado); // 201: Created
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<String> token = usuarioService.verificarCredenciales(loginRequest.getCorreo(), loginRequest.getClave());
        return ResponseEntity.status(200).body(token.orElse(null));
        // Devuelve el 200: OK y el JWT que expirará en 30 días
    }

    @PostMapping("/logout")
    public ResponseEntity<String> cerrarSesion(){
        return ResponseEntity.ok("Sesion cerrada exitosamente.");
        // Aquí desde el Front se debería borrar el JWT
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/ebp08/gestion_financiera_backend/entity/Usuario#