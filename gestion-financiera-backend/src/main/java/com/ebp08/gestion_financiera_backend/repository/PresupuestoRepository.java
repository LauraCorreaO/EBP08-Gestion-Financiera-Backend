package com.ebp08.gestion_financiera_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebp08.gestion_financiera_backend.entity.Presupuesto;
import java.util.List;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    List<Presupuesto> findByUsuarioId(Long idUsuario);

}
