package com.ebp08.gestion_financiera_backend.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor

public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> crearCategoriaPersonalizada(@RequestBody Categoria categoria) { //@RequestBody toma la petición HTTP y la convierte en un objeto Categoria.
        Categoria categoriaNueva = categoriaService.crearCategoriaPersonalizada(categoria);
        
        return ResponseEntity.status(201).body(categoriaNueva); // 201: Created

    }

    @GetMapping("/usuario/{idUsuario}") //ruta para obtener las categorías de un usuario específico. {idUsuario} es un valor dinámico que se pasará en la URL.
    public ResponseEntity<List<Categoria>> obtenerCategoriasUsuario(@PathVariable Long idUsuario) { // el @pathvariable toma el valor que haya en {idUsuario} para usar ese usuario específico y obtener sus categorías.
        List<Categoria> categorias = categoriaService.obtenerCategoriasUsuario(idUsuario);
        
        return ResponseEntity.status(200).body(categorias); // 200: OK
    }

    @DeleteMapping("/idCategoria") //ruta para eliminar una categoría específica. 
    public ResponseEntity<Void>eliminarCategoriaPersonalizada(@PathVariable long idCategoria){ // el @pathvariable toma el valor que haya en {idCategoria} para usar esa categoría específica y eliminarla.
        categoriaService.eliminarCategoriaPersonalizada(idCategoria);
        return ResponseEntity.status(204).build(); // 204: No Content
    }

    


}
