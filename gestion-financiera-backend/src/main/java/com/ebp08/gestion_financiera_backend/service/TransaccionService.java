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
import com.ebp08.gestion_financiera_backend.repository.UsuarioRepository; // Importa el repositorio de usuarios para validar que el usuario exista.

import lombok.AllArgsConstructor; // Importa Lombok para generar automáticamente un constructor con todos los atributos final.

@Service // Le dice a Spring que esta clase es un servicio y contiene lógica de negocio.
@AllArgsConstructor // Lombok genera automáticamente un constructor con todos los atributos final de la clase.
public class TransaccionService { // Define la clase de servicio para manejar la lógica relacionada con transacciones.

    private final TransaccionRepository transaccionRepository; // Repositorio para operaciones de base de datos sobre transacciones.
    private final CategoriaRepository categoriaRepository; // Repositorio para buscar categorías existentes.
    private final UsuarioRepository usuarioRepository; // Repositorio para buscar usuarios existentes.

    public Transaccion crearTransaccion(CrearTransaccionRequest request) { // Método para crear una nueva transacción a partir de los datos del DTO.

        if (request.getIdUsuario() == null) { // Valida que el request traiga el id del usuario.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar un usuario válido."); // Si no trae usuario, responde error 400.
        }

        if (request.getIdCategoria() == null) { // Valida que el request traiga el id de la categoría.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar una categoría válida."); // Si no trae categoría, responde error 400.
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

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario()) // Busca en la base de datos el usuario con el id enviado en el request.
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.")); // Si no existe, responde error 404.

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria()) // Busca en la base de datos la categoría con el id enviado en el request.
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada.")); // Si no existe, responde error 404.

        if (categoria.getUsuario() == null || categoria.getUsuario().getId() == null) { // Valida que la categoría tenga un usuario asociado.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La transacción solo puede usar categorías personalizadas del usuario."); // Si no tiene usuario, responde error 400.
        }

        if (!categoria.getUsuario().getId().equals(usuario.getId())) { // Valida que la categoría pertenezca realmente al mismo usuario de la transacción.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La categoría no pertenece al usuario de la transacción."); // Si pertenece a otro usuario, responde error 403.
        }

        Transaccion transaccion = new Transaccion(); // Crea un nuevo objeto Transaccion vacío.

        transaccion.setUsuario(usuario); // Asigna a la transacción el usuario real obtenido desde la base de datos.
        transaccion.setCategoria(categoria); // Asigna a la transacción la categoría real obtenida desde la base de datos.
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

        if (!usuarioRepository.existsById(idUsuario)) { // Verifica si el usuario existe realmente en la base de datos.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."); // Si no existe, responde error 404.
        }

        return transaccionRepository.findByUsuarioId(idUsuario); // Busca y devuelve todas las transacciones que pertenezcan a ese usuario.
    }

    public void eliminarTransaccion(Long idTransaccion, Long idUsuario) { // Método para eliminar una transacción específica de un usuario específico.

        Transaccion transaccion = transaccionRepository.findByIdAndUsuarioId(idTransaccion, idUsuario) // Busca la transacción por id y además valida que pertenezca a ese usuario.
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada para ese usuario.")); // Si no existe o no pertenece al usuario, responde error 404.

        transaccionRepository.delete(transaccion); // Elimina la transacción encontrada de la base de datos.
    }
}