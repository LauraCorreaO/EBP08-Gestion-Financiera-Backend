package com.ebp08.gestion_financiera_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebp08.gestion_financiera_backend.entity.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    
}
