package com.ebp08.gestion_financiera_backend.controller;


import com.ebp08.gestion_financiera_backend.dto.ActualizarCategoriaRequest;
import com.ebp08.gestion_financiera_backend.dto.CrearCategoriaRequest;
import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.service.CategoriaService;
import com.ebp08.gestion_financiera_backend.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor

public class CategoriaController {

    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    @PostMapping("/crearCategoriaPropia") //ruta para crear una categoría personalizada para un usuario específico.
    public ResponseEntity<Categoria> crearCategoriaPersonalizada(@Valid @RequestBody CrearCategoriaRequest request) {
        Categoria categoriaNueva = categoriaService.crearCategoriaPersonalizada(request);
        
        return ResponseEntity.status(201).body(categoriaNueva); // 201: Created
    }

    @GetMapping("/usuario") //ruta para obtener las categorías de un usuario específico. El id del usuario se obtiene del token de autenticación, no se recibe por parámetro.
    public ResponseEntity<List<Categoria>> obtenerCategoriasUsuario() { 
        List<Categoria> categorias = categoriaService.obtenerCategoriasUsuario();
        return ResponseEntity.status(200).body(categorias); // 200: OK
    }

    @PutMapping("/actualizarCategoriaPropia") //ruta para actualizar una categoría personalizada. Se recibe un JSON con los datos de la categoría a actualizar, incluyendo su id para identificarla.
    public ResponseEntity<Categoria> actualizarCategoriaPersonalizada(@Valid @RequestBody ActualizarCategoriaRequest request) {
        Categoria categoriaActualizada = categoriaService.actualizarCategoriaPersonalizada(request);
        
        return ResponseEntity.status(200).body(categoriaActualizada); // 200: OK
    }

    @DeleteMapping("/eliminarCategoriaPropia/{idCategoria}") //ruta para eliminar una categoría específica.
    public ResponseEntity<Void>eliminarCategoriaPersonalizada(@PathVariable long idCategoria){ // el @pathvariable toma el valor que haya en {idCategoria} para usar esa categoría específica y eliminarla.
        categoriaService.eliminarCategoriaPersonalizada(idCategoria);
        return ResponseEntity.status(204).build(); // 204: No Content
    }

    


}
