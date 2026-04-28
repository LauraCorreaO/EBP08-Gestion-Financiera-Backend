package com.ebp08.gestion_financiera_backend.repository; 

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.ebp08.gestion_financiera_backend.entity.Transaccion; //Importa la entidad Transaccion, porque este repositorio va a trabajar con esa tabla/entidad.
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByUsuarioId(Long idUsuario); // busca todas las transacciones que pertenezcan a un usuario específico, usando el id del usuario como filtro.

    Optional<Transaccion> findByIdAndUsuarioId(Long idTransaccion, Long idUsuario); // Busca una transacción específica que además pertenezca a ese usuario 

    // para el balance mensual acumulado y para el reporte de transacciones por fecha
    List<Transaccion> findByUsuarioIdAndFechaBetween(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin); // Busca todas las transacciones de un usuario específico que hayan ocurrido entre dos fechas dadas (fechaInicio y fechaFin).

    List<Transaccion> findByCategoriaId(Long idCategoria);

    List<Transaccion> findByUsuarioIdAndTipoOrderByFechaDesc(Long idUsuario, TipoTransaccion tipo);

    List<Transaccion> findByUsuarioIdAndTipoAndFechaBetweenOrderByFechaDesc(Long idUsuario, TipoTransaccion tipo, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
