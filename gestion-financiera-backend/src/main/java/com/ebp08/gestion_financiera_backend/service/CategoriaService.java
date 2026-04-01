package com.ebp08.gestion_financiera_backend.service;

import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service

@AllArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public Categoria crearCategoriaPersonalizada(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoría no puede ser vacío.");
        }
        if (categoria.getTipo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de la categoría no puede ser nulo.");
        }
        if (categoria.getUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría personalizada debe estar asociada a un usuario.");
        }
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerCategoriasUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del usuario no puede ser nulo.");
        }
        return categoriaRepository.findByUsuarioIsNullOrUsuarioId(idUsuario);
    }

    public void eliminarCategoriaPersonalizada(Long idCategoria){
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada."));
                        // O si no, tira (() -> Exception)

        if (categoria.getUsuario() == null) {  // OJO con eliminar categorías globales
            throw new ResponseStatusException(HttpStatus.FORBIDDEN ,"No se puede eliminar una categoría global.");
        }

        categoriaRepository.deleteById(idCategoria);
    }
}
