package com.ebp08.gestion_financiera_backend.repository;

import com.ebp08.gestion_financiera_backend.entity.TransaccionProgramada;
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransaccionProgramadaRepository extends JpaRepository<TransaccionProgramada, Long> {

    List<TransaccionProgramada> findByUsuarioId(Long idUsuario);

    List<TransaccionProgramada> findByUsuarioIdAndTipo(Long idUsuario, TipoTransaccion tipo);

    Optional<TransaccionProgramada> findByIdAndUsuarioId(Long id, Long idUsuario);
}