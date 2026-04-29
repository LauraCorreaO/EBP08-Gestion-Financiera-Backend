package com.ebp08.gestion_financiera_backend.service;

import com.ebp08.gestion_financiera_backend.dto.ActualizarTransaccionProgramadaRequest;
import com.ebp08.gestion_financiera_backend.dto.CrearTransaccionProgramadaRequest;
import com.ebp08.gestion_financiera_backend.entity.Categoria;
import com.ebp08.gestion_financiera_backend.entity.TransaccionProgramada;
import com.ebp08.gestion_financiera_backend.entity.Usuario;
import com.ebp08.gestion_financiera_backend.enums.Estado;
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository;
import com.ebp08.gestion_financiera_backend.repository.TransaccionProgramadaRepository;
import com.ebp08.gestion_financiera_backend.security.SecurityHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransaccionProgramadaService {

    private final TransaccionProgramadaRepository transaccionProgramadaRepository;
    private final CategoriaRepository categoriaRepository;
    private final SecurityHelper securityHelper;

    public TransaccionProgramada crearTransaccionProgramada(CrearTransaccionProgramadaRequest request) {

        if (request.getTipo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar un tipo de transacción válido.");
        }

        if (request.getFrecuencia() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar una frecuencia válida.");
        }

        if (request.getIdCategoria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar una categoría válida.");
        }

        if (request.getFechaInicio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de inicio es obligatoria.");
        }

        // Validar y convertir monto — mismo patrón que TransaccionService
        if (request.getMonto() == null || request.getMonto().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto es obligatorio.");
        }

        BigDecimal monto;
        try {
            monto = new BigDecimal(request.getMonto().trim());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe tener un formato numérico válido.");
        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero.");
        }

        // Validar fechaFin si se envía
        if (request.getFechaFin() != null && request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada."));

        // Validar que la categoría pertenezca al usuario o sea global — mismo criterio que TransaccionService
        if (categoria.getUsuario() != null && !categoria.getUsuario().getId().equals(usuarioAutenticado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para usar esta categoría.");
        }

        // GRASP Creator: el service construye la entidad porque tiene acceso
        // a todos los colaboradores necesarios (usuario, categoría, datos del request).
        TransaccionProgramada nueva = new TransaccionProgramada();
        nueva.setUsuario(usuarioAutenticado);
        nueva.setCategoria(categoria);
        nueva.setTipo(request.getTipo());
        nueva.setFrecuencia(request.getFrecuencia());
        nueva.setMonto(monto);
        nueva.setFechaInicio(request.getFechaInicio());
        nueva.setFechaFin(request.getFechaFin());
        nueva.setEstado(Estado.ACTIVO);
        nueva.setUltimaEjecucion(null); // null hasta que @Scheduled lo gestione en sprint futuro

        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            nueva.setDescripcion("");
        } else {
            nueva.setDescripcion(request.getDescripcion().trim());
        }

        return transaccionProgramadaRepository.save(nueva);
    }

    public TransaccionProgramada actualizarTransaccionProgramada(Long id, ActualizarTransaccionProgramadaRequest request) {

        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();

        // Busca y valida propiedad en una sola consulta — mismo patrón que TransaccionRepository
        TransaccionProgramada programada = transaccionProgramadaRepository
                .findByIdAndUsuarioId(id, usuarioAutenticado.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Transacción programada no encontrada o no te pertenece."));

        if (request.getMonto() != null && !request.getMonto().trim().isEmpty()) {
            BigDecimal monto;
            try {
                monto = new BigDecimal(request.getMonto().trim());
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe tener un formato numérico válido.");
            }
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero.");
            }
            programada.setMonto(monto);
        }

        if (request.getDescripcion() != null) {
            programada.setDescripcion(request.getDescripcion().trim());
        }

        if (request.getFrecuencia() != null) {
            programada.setFrecuencia(request.getFrecuencia());
        }

        if (request.getEstado() != null) {
            programada.setEstado(request.getEstado());
        }

        if (request.getFechaInicio() != null) {
            programada.setFechaInicio(request.getFechaInicio());
        }

        if (request.getFechaFin() != null) {
            // Valida contra la fechaInicio ya almacenada o la nueva si viene en el mismo request
            LocalDate inicioEfectivo = request.getFechaInicio() != null
                    ? request.getFechaInicio()
                    : programada.getFechaInicio();

            if (request.getFechaFin().isBefore(inicioEfectivo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La fecha de fin no puede ser anterior a la fecha de inicio.");
            }
            programada.setFechaFin(request.getFechaFin());
        }

        return transaccionProgramadaRepository.save(programada);
    }

    public void eliminarTransaccionProgramada(Long id) {

        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();

        TransaccionProgramada programada = transaccionProgramadaRepository
                .findByIdAndUsuarioId(id, usuarioAutenticado.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Transacción programada no encontrada o no te pertenece."));

        transaccionProgramadaRepository.delete(programada);
    }

    public List<TransaccionProgramada> listarTransaccionesProgramadas() {

        Long idUsuario = securityHelper.obtenerUsuarioAutenticado().getId();
        return transaccionProgramadaRepository.findByUsuarioId(idUsuario);
    }

    public List<TransaccionProgramada> listarTransaccionesProgramadasPorTipo(TipoTransaccion tipo) {

        Long idUsuario = securityHelper.obtenerUsuarioAutenticado().getId();
        return transaccionProgramadaRepository.findByUsuarioIdAndTipo(idUsuario, tipo);
    }
}