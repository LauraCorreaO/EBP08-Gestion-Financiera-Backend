package com.ebp08.gestion_financiera_backend.service; // Indica que esta clase pertenece al paquete service, donde va la lógica de negocio.

import java.math.BigDecimal; // Importa BigDecimal para manejar valores monetarios con precisión y evitar errores de decimales.
import java.time.LocalDateTime; // Importa LocalDateTime para guardar la fecha y hora en la que se crea la transacción.
import java.util.List; // Importa List porque más abajo tenemos un método que devuelve una lista de transacciones.

import org.springframework.http.HttpStatus; // Importa los códigos de estado HTTP, como 400, 403 y 404.
import org.springframework.stereotype.Service; // Importa la anotación @Service para marcar esta clase como un servicio de Spring.
import org.springframework.web.server.ResponseStatusException; // Importa la excepción que permite devolver errores HTTP con mensaje personalizado.

import com.ebp08.gestion_financiera_backend.dto.CrearTransaccionRequest; // Importa el DTO que contiene los datos necesarios para crear una transacción.
import com.ebp08.gestion_financiera_backend.entity.Categoria; // Importa la entidad Categoria porque una transacción debe pertenecer a una categoría.
import com.ebp08.gestion_financiera_backend.entity.Transaccion; // Importa la entidad Transaccion porque este servicio crea, consulta y elimina transacciones.
import com.ebp08.gestion_financiera_backend.entity.Usuario; // Importa la entidad Usuario porque una transacción debe pertenecer a un usuario.
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository; // Importa el repositorio de categorías para buscar categorías en la base de datos.
import com.ebp08.gestion_financiera_backend.repository.TransaccionRepository; // Importa el repositorio de transacciones para guardar, buscar y borrar transacciones.
import com.ebp08.gestion_financiera_backend.enums.TipoTransaccion;
import com.ebp08.gestion_financiera_backend.security.SecurityHelper; // Importa el helper de seguridad para obtener el usuario autenticado.

import lombok.AllArgsConstructor; // Importa Lombok para generar automáticamente un constructor con todos los atributos final.

@Service // Le dice a Spring que esta clase es un servicio y contiene lógica de negocio.
@AllArgsConstructor // Lombok genera automáticamente un constructor con todos los atributos final de la clase.
public class TransaccionService { // Define la clase de servicio para manejar la lógica relacionada con transacciones.

    private final TransaccionRepository transaccionRepository; // Repositorio para operaciones de base de datos sobre transacciones.
    private final CategoriaRepository categoriaRepository; // Repositorio para buscar categorías existentes.
    private final SecurityHelper securityHelper; // Helper de seguridad para obtener el usuario autenticado.

    public Transaccion crearTransaccion(CrearTransaccionRequest request) { // Método para crear una nueva transacción a partir de los datos del DTO.

        if (request.getTipo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar un tipo de transacción válido.");
        }

        if (request.getMonto() == null || request.getMonto().trim().isEmpty()) { // Valida que el monto no sea nulo ni venga vacío.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto es obligatorio."); // Si viene vacío, responde error 400.
        }

        BigDecimal monto; // Declara una variable BigDecimal para guardar el monto convertido desde texto.

        try { // Intenta convertir el monto recibido como String a BigDecimal.
            monto = new BigDecimal(request.getMonto().trim()); // Convierte el texto del monto a BigDecimal quitando espacios al inicio y al final.
        } catch (NumberFormatException e) { // Si la conversión falla porque el texto no es un número válido...
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe tener un formato numérico válido."); // ...responde error 400 indicando que el monto es inválido.
        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) { // Valida que el monto sea mayor que cero.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero."); // Si es cero o negativo, responde error 400.
        }

        
        Usuario usuarioAutenticado = securityHelper.obtenerUsuarioAutenticado();

        Categoria categoria = obtenerCategoriaSolicitadaOPorDefecto(request.getIdCategoria(), usuarioAutenticado);

        Transaccion transaccion = new Transaccion(); // Crea un nuevo objeto Transaccion vacío.

        transaccion.setUsuario(usuarioAutenticado); // Asigna el usuario autenticado a la transacción.
        transaccion.setCategoria(categoria); // Asigna a la transacción la categoría real obtenida desde la base de datos.
        transaccion.setTipo(request.getTipo());
        transaccion.setMonto(monto); // Asigna el monto ya validado y convertido.
        transaccion.setFecha(LocalDateTime.now()); // Asigna automáticamente la fecha y hora actual del sistema.

        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) { // Verifica si la descripción vino nula o vacía.
            transaccion.setDescripcion(""); // Si está vacía, asigna una cadena vacía para evitar null.
        } else { // Si la descripción sí vino con contenido...
            transaccion.setDescripcion(request.getDescripcion().trim()); // ...la guarda quitando espacios sobrantes al inicio y al final.
        }

        return transaccionRepository.save(transaccion); // Guarda la transacción en la base de datos y devuelve la entidad ya persistida.
    }

    public List<Transaccion> obtenerTransaccionesUsuario(Long idUsuario) { // Método para listar todas las transacciones de un usuario específico.

        if (idUsuario == null) { // Valida que el id del usuario no sea nulo.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del usuario no puede ser nulo."); // Si es nulo, responde error 400.
        }

        // Validar que el usuario autenticado sea quien solicita sus propias transacciones
        securityHelper.validarPropiedad(idUsuario);

        return transaccionRepository.findByUsuarioId(idUsuario); // Busca y devuelve todas las transacciones que pertenezcan a ese usuario.
    }

    public List<Transaccion> obtenerIngresosRecientes(Long idUsuario) {
        if (idUsuario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del usuario no puede ser nulo.");
        }

        securityHelper.validarPropiedad(idUsuario);

        return transaccionRepository.findByUsuarioIdAndTipoOrderByFechaDesc(idUsuario, TipoTransaccion.INGRESO);
    }

    private Categoria obtenerCategoriaSolicitadaOPorDefecto(Long idCategoria, Usuario usuarioAutenticado) {
        if (idCategoria == null) {
            return categoriaRepository.findByNombreIgnoreCaseAndUsuarioIsNull("OTROS")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No existe la categoría global OTROS."));
        }

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada."));

        // Validar que la categoría pertenezca al usuario autenticado (o sea una categoría global)
        if (categoria.getUsuario() != null && !categoria.getUsuario().getId().equals(usuarioAutenticado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para usar esta categoría.");
        }

        return categoria;
    }

    public void eliminarTransaccion(Long idTransaccion, Long idUsuario) {
        securityHelper.validarPropiedad(idUsuario);

        Transaccion transaccion = transaccionRepository.findByIdAndUsuarioId(idTransaccion, idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacci�n no encontrada para ese usuario."));

        transaccionRepository.delete(transaccion);
    }
}

