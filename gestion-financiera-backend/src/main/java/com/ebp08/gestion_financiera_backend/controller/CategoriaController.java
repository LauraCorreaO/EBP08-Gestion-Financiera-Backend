package com.ebp08.gestion_financiera_backend.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.service.CategoriaService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor

public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> crearCategoriaPersonalizada(@RequestBody Categoria categoria) {
        Categoria categoriaNueva = categoriaService.crearCategoriaPersonalizada(categoria);
        
        return ResponseEntity.status(201).body(categoriaNueva); // 201: Created

    }
    


}
