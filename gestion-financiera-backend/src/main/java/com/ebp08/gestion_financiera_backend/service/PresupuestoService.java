package com.ebp08.gestion_financiera_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.entity.Presupuesto;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
import com.ebp08.gestion_financiera_backend.repository.PresupuestoRepository;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor


public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    
    // crear un presupuesto global 
    public Presupuesto crearPresupuestoGlobal(Presupuesto presupuesto) {
    
    // Busca si ya existe un presupuesto global para este usuario en el mes actual
    Optional<Presupuesto> existente = presupuestoRepository
        .findByUsuarioIdAndMesActual(presupuesto.getUsuario().getId());

    if (existente.isPresent()) {
        // Si existe, actualiza el monto del mismo
        Presupuesto presupuestoActual = existente.get();
        presupuestoActual.setMontoLimite(presupuesto.getMontoLimite());
        return presupuestoRepository.save(presupuestoActual); // Actualiza el presupuesto existente
    }

    // Si no existe, asigna fecha límite y crea uno nuevo
    presupuesto.setFechaLimite(LocalDateTime.now()
        .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
        .withHour(23).withMinute(59).withSecond(59));

    return presupuestoRepository.save(presupuesto); // Crea un nuevo presupuesto global
    
    }
    // Crear un presupuesto específico para una categoría
    public Presupuesto crearPresupuestoCategoria(Presupuesto presupuesto) {
        
        Categoria categoria = presupuesto.getCategoria();
        Usuario usuario = presupuesto.getUsuario();

        // Validación: la categoría debe pertenecer al usuario
        if (categoria.getUsuario() == null || 
            !categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "La categoría no pertenece al usuario.");
        }

        // Buscar si ya existe un presupuesto para esta categoría en el mes actual
        Optional<Presupuesto> existente = presupuestoRepository
            .findByUsuarioIdAndCategoriaIdAndMesActual(usuario.getId(), categoria.getId());

        if (existente.isPresent()) {
            // Si existe, actualiza el monto y la fecha límite
            Presupuesto presupuestoActual = existente.get();
            presupuestoActual.setMontoLimite(presupuesto.getMontoLimite());
            presupuestoActual.setFechaLimite(presupuesto.getFechaLimite());
            return presupuestoRepository.save(presupuestoActual); // Actualiza el presupuesto existente
        }

        // Si no existe, crea uno nuevo
        return presupuestoRepository.save(presupuesto); // Crea un nuevo presupuesto por categoría
    }

    public List<Presupuesto> obtenerPresupuestoUsuario(Long idUsuario) {
        // para obtener el presupuesto de un usuario.
        return presupuestoRepository.findByUsuarioId(idUsuario);
    }

    /*public boolean verificarLimiteAlcanzado(Long idPresupuesto){
        
    }*/
    

}
