package com.ebp08.gestion_financiera_backend.service;

import com.ebp08.gestion_financiera_backend.dto.ActualizarCategoriaRequest;
import com.ebp08.gestion_financiera_backend.dto.CrearCategoriaRequest;
import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository;
import com.ebp08.gestion_financiera_backend.security.SecurityHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SecurityHelper securityHelper;

    public Categoria crearCategoriaPersonalizada(CrearCategoriaRequest request) {
       
        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();

        // Crear la categoría
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setUsuario(usuarioAutenticado);

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerCategoriasUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del usuario no puede ser nulo.");
        }
        
        // Validar que el usuario autenticado sea quien solicita sus propias categorías
        securityHelper.validarPropiedad(idUsuario);
        
        return categoriaRepository.findByUsuarioIsNullOrUsuarioId(idUsuario);
    }

   /*  public Categoria actualizarCategoriaPersonalizada(ActualizarCategoriaRequest request) {
        // Buscar la categoría por ID
        Categoria categoria = categoriaRepository.findById(request.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada."));

        // Validar que no sea una categoría global
        if (categoria.getUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede actualizar una categoría global.");
        }

        // Validar que el usuario autenticado sea el propietario de la categoría
        securityHelper.validarPropiedad(categoria.getUsuario().getId());

        // Actualizar los campos
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoriaPersonalizada(Long idCategoria){
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada."));

        if (categoria.getUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede eliminar una categoría global.");
        }

        // Validar que el usuario autenticado sea el propietario de la categoría
        securityHelper.validarPropiedad(categoria.getUsuario().getId());

        categoriaRepository.deleteById(idCategoria);
    }*/
}
