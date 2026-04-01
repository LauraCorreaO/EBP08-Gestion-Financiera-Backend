package com.ebp08.gestion_financiera_backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ebp08.gestion_financiera_backend.entity.Presupuesto;
import com.ebp08.gestion_financiera_backend.repository.PresupuestoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor


public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    

    public Presupuesto crearPresupuestoGlobal(Presupuesto presupuesto) {
        // crear un presupuesto global .
        return presupuestoRepository.save(presupuesto);
    }

    public Presupuesto crearPresupuestoCategoria(Presupuesto presupuesto) {
        //  crear un presupuesto específico para una categoría.
        return presupuestoRepository.save(presupuesto);
    }

    public List<Presupuesto> obtenerPresupuestoUsuario(Long idUsuario) {
        // para obtener el presupuesto de un usuario.
        return presupuestoRepository.findByUsuarioId(idUsuario);
    }

    /*public boolean verificarLimiteAlcanzado(Long idPresupuesto){
        
    }*/
    

}
