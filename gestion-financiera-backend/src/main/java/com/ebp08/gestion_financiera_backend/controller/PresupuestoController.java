package com.ebp08.gestion_financiera_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebp08.gestion_financiera_backend.entity.Presupuesto;
import com.ebp08.gestion_financiera_backend.service.PresupuestoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.ebp08.gestion_financiera_backend.dto.CrearPresupuestoCategoriaRequest;
import com.ebp08.gestion_financiera_backend.dto.CrearPresupuestoGlobalRequest;


@RestController
@RequestMapping("/api/presupuestos")
@AllArgsConstructor

public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    @PostMapping("/global") //ruta para crear un presupuesto global.
    public ResponseEntity<Presupuesto>crearPresupuestoGlobal(@Valid@RequestBody CrearPresupuestoGlobalRequest request) {
        Presupuesto presupuestoNuevoGlobal = presupuestoService.crearPresupuestoGlobal(request);
        return ResponseEntity.status(201).body(presupuestoNuevoGlobal);// 201: Created
    } 

     @PostMapping("/categoria") //ruta para crear un presupuesto específico para una categoría.
    public ResponseEntity<Presupuesto>crearPresupuestoCategoria(@Valid@RequestBody CrearPresupuestoCategoriaRequest request) {
        Presupuesto presupuestoNuevoPorCategoria = presupuestoService.crearPresupuestoCategoria(request);
        return ResponseEntity.status(201).body(presupuestoNuevoPorCategoria); // 201: Created
    }


    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity <List<Presupuesto>>obtenerPresupuestoUsuario(@PathVariable Long idUsuario){
        List<Presupuesto> presupuestosUsuario = presupuestoService.obtenerPresupuestoUsuario(idUsuario);
        return ResponseEntity.status(200).body(presupuestosUsuario); // 200: OK

    }
     




}
    


