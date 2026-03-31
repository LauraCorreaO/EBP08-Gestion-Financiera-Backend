package com.ebp08.gestion_financiera_backend.service;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.entity.Transaccion;
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository;
import com.ebp08.gestion_financiera_backend.repository.TransaccionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransaccionService {
    
    private final TransaccionRepository transaccionRepository;
    private final CategoriaRepository categoriaRepository;

    public Transaccion crearTransaccion(Transaccion transaccion) {
        
        if (transaccion.getCategoria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La transacción debe tener una categoría.");
        }

        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto de la transacción debe ser mayor a cero.");
        }

        return transaccionRepository.save(transaccion);
    }

    public void eliminarTransaccion(Long idTransaccion) {
        
        if (!transaccionRepository.existsById(idTransaccion)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada.");
        }

        transaccionRepository.deleteById(idTransaccion);
    }

    public Transaccion asignarCategoriaTransaccion(Long idTransaccion, Long idCategoria) {
        
        Transaccion transaccion = transaccionRepository.findById(idTransaccion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada."));
        
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada."));
        
        transaccion.setCategoria(categoria);
        
        return transaccionRepository.save(transaccion);
    }

}
