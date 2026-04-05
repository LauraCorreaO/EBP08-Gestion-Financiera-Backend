package com.ebp08.gestion_financiera_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ebp08.gestion_financiera_backend.dto.CrearPresupuestoCategoriaRequest;
import com.ebp08.gestion_financiera_backend.dto.CrearPresupuestoGlobalRequest;
import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.entity.Presupuesto;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository;
import com.ebp08.gestion_financiera_backend.repository.PresupuestoRepository;
import com.ebp08.gestion_financiera_backend.security.SecurityHelper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SecurityHelper securityHelper;
    
    // crear un presupuesto global 
    public Presupuesto crearPresupuestoGlobal(CrearPresupuestoGlobalRequest request) {
        
        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();
        
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setUsuario(usuarioAutenticado);
        presupuesto.setMontoLimite(request.getMontoLimite());
        presupuesto.setCategoria(null); // Un presupuesto global no tiene categoría
    
        // Busca si ya existe un presupuesto global para este usuario en el mes actual
        Optional<Presupuesto> existente = presupuestoRepository
            .findByUsuarioIdAndMesActual(presupuesto.getUsuario().getId());

        if (existente.isPresent()) {
            // Si existe, actualiza el monto del mismo
            Presupuesto presupuestoActual = existente.get();
            presupuestoActual.setMontoLimite(presupuesto.getMontoLimite());
            presupuestoActual.setFechaLimite(LocalDateTime.now()
                .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59));
            return presupuestoRepository.save(presupuestoActual); // Actualiza el presupuesto existente
        }

        // Si no existe, asigna fecha límite y crea uno nuevo
        presupuesto.setFechaLimite(LocalDateTime.now()
            .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
            .withHour(23).withMinute(59).withSecond(59));

        return presupuestoRepository.save(presupuesto); // Crea un nuevo presupuesto global
    }
    // Crear un presupuesto específico para una categoría
    public Presupuesto crearPresupuestoCategoria(CrearPresupuestoCategoriaRequest request) {
        
        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();
        
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setUsuario(usuarioAutenticado);
        presupuesto.setCategoria(categoriaRepository.findById(request.getIdCategoria()).orElse(null));
        presupuesto.setMontoLimite(request.getMontoLimite());

        Categoria categoria = presupuesto.getCategoria();
        Usuario usuario = presupuesto.getUsuario();

        // Validación: la categoría debe pertenecer al usuario autenticado
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
            presupuestoActual.setFechaLimite(LocalDateTime.now()
                .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59));
            return presupuestoRepository.save(presupuestoActual); // Actualiza el presupuesto existente
        }

        // Si no existe, asigna fecha límite y crea uno nuevo
        presupuesto.setFechaLimite(LocalDateTime.now()
            .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
            .withHour(23).withMinute(59).withSecond(59));
        
        return presupuestoRepository.save(presupuesto); // Crea un nuevo presupuesto por categoría
    }

    public List<Presupuesto> obtenerPresupuestoUsuario(Long idUsuario) {
        // Validar que el usuario autenticado sea quien solicita sus propios presupuestos
        securityHelper.validarPropiedad(idUsuario);
        
        // para obtener el presupuesto de un usuario.
        return presupuestoRepository.findByUsuarioId(idUsuario);
    }

    /*public boolean verificarLimiteAlcanzado(Long idPresupuesto){
        
    }*/
    

}
