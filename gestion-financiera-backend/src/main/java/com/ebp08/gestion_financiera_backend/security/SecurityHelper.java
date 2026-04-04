package com.ebp08.gestion_financiera_backend.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.ebp08.gestion_financiera_backend.entity.Usuario;
import com.ebp08.gestion_financiera_backend.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SecurityHelper {

    private final UsuarioRepository usuarioRepository;

    // Método para obtener el usuario autenticado a partir del contexto de seguridad.
    public Usuario obtenerUsuarioAutenticado() {
        String correoAutenticado = (String) SecurityContextHolder.getContext()
                                                    .getAuthentication()
                                                    .getPrincipal(); // 

        if (correoAutenticado == null || correoAutenticado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado.");
        }

        return usuarioRepository.findByCorreo(correoAutenticado)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario autenticado no encontrado en la base de datos."));
    }

    // Método para validar que el usuario autenticado es el propietario de un recurso (como sus propias transacciones, categorías o presupuestos).
    public void validarPropiedad(Long idRecurso) {
        Usuario usuarioAutenticado = obtenerUsuarioAutenticado();
        
        if (!usuarioAutenticado.getId().equals(idRecurso)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a este recurso.");
        }
    }
}
