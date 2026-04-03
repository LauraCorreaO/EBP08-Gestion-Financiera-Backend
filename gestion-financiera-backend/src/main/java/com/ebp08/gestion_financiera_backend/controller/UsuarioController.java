package com.ebp08.gestion_financiera_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebp08.gestion_financiera_backend.dto.LoginRequest;
import com.ebp08.gestion_financiera_backend.dto.RegistroRequest;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
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
        return ResponseEntity.status(200).body(token.orElse(null)); // 200: OK
    }
}
