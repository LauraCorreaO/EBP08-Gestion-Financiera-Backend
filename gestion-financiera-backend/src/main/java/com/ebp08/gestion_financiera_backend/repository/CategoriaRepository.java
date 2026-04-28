package com.ebp08.gestion_financiera_backend.repository;

import com.ebp08.gestion_financiera_backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUsuarioIsNullOrUsuarioId(Long idUsuario);

    /* JPA entiende y traduce a SQL
        SELECT * FROM categoria
        WHERE usuario_id IS NULL
        OR usuario_id = ?
     */
    Optional<Categoria> findByNombreIgnoreCaseAndUsuarioIsNull(String nombre);
}
