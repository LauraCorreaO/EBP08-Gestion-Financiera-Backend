package com.ebp08.gestion_financiera_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
