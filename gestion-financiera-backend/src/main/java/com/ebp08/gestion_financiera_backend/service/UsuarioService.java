package com.ebp08.gestion_financiera_backend.service;

import org.springframework.stereotype.Service;

import com.ebp08.gestion_financiera_backend.entity.Usuario;
import com.ebp08.gestion_financiera_backend.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario usuario) {
        // lógica para crear un nuevo usuario.
        return usuarioRepository.save(usuario);
    }
}
