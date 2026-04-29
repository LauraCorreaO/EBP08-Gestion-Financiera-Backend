package com.ebp08.gestion_financiera_backend.controller;

import com.ebp08.gestion_financiera_backend.dto.ActualizarTransaccionProgramadaRequest;
import com.ebp08.gestion_financiera_backend.dto.CrearTransaccionProgramadaRequest;
import com.ebp08.gestion_financiera_backend.entity.TransaccionProgramada;
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;
import com.ebp08.gestion_financiera_backend.service.TransaccionProgramadaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones-programadas")
@AllArgsConstructor
public class TransaccionProgramadaController {

    private final TransaccionProgramadaService transaccionProgramadaService;

    @PostMapping
    public ResponseEntity<TransaccionProgramada> crear(
            @RequestBody CrearTransaccionProgramadaRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaccionProgramadaService.crearTransaccionProgramada(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionProgramada> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarTransaccionProgramadaRequest request) {
        return ResponseEntity.ok(transaccionProgramadaService.actualizarTransaccionProgramada(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        transaccionProgramadaService.eliminarTransaccionProgramada(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransaccionProgramada>> listar(
            @RequestParam(required = false) TipoTransaccion tipo) {
        List<TransaccionProgramada> resultado = (tipo != null)
                ? transaccionProgramadaService.listarTransaccionesProgramadasPorTipo(tipo)
                : transaccionProgramadaService.listarTransaccionesProgramadas();
        return ResponseEntity.ok(resultado);
    }
}