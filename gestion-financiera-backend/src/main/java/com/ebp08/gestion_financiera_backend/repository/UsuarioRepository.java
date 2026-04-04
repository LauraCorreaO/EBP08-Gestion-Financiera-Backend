package com.ebp08.gestion_financiera_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Hace un SELECT COUNT(*) > 0 FROM usuario WHERE correo = ?
    boolean existsByCorreo(String correo);

    // Hace un SELECT * FROM usuario WHERE correo = ?
    Optional<Usuario> findByCorreo(String correo);
}
