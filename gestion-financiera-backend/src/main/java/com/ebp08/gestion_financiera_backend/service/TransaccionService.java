package com.ebp08.gestion_financiera_backend.service;// Indica que esta clase pertenece al paquete service, donde va la lógica de negocio.

import java.math.BigDecimal; // Importa BigDecimal para manejar valores monetarios con precisión.
import java.time.LocalDateTime; // Importa LocalDateTime para manejar fecha y hora de la transacción.
import java.util.List; // Importa List porque vamos a devolver listas de transacciones.

import org.springframework.http.HttpStatus; // Importa los códigos de estado HTTP, como 400, 403, 404.
import org.springframework.stereotype.Service; // Importa la anotación @Service para marcar esta clase como servicio de Spring.
import org.springframework.web.server.ResponseStatusException; // Importa la excepción que permite lanzar errores HTTP con mensaje.

import com.ebp08.gestion_financiera_backend.entity.Categoria; // Importa la entidad Categoria porque una transacción pertenece a una categoría.
import com.ebp08.gestion_financiera_backend.entity.Transaccion; // Importa la entidad Transaccion porque esta clase trabaja con transacciones.
import com.ebp08.gestion_financiera_backend.entity.Usuario; // Importa la entidad Usuario porque una transacción pertenece a un usuario.
import com.ebp08.gestion_financiera_backend.repository.CategoriaRepository; // Importa el repositorio de Categoria para buscar categorías en la base de datos.
import com.ebp08.gestion_financiera_backend.repository.TransaccionRepository; // Importa el repositorio de Transaccion para guardar, buscar y borrar transacciones.
import com.ebp08.gestion_financiera_backend.repository.UsuarioRepository; // Importa el repositorio de Usuario para validar que el usuario exista.

import lombok.AllArgsConstructor; // Importa Lombok para generar automáticamente un constructor con todos los atributos.

@Service // Le dice a Spring que esta clase es un servicio y contiene lógica de negocio.
@AllArgsConstructor // Lombok genera un constructor con todos los atributos final de la clase.
public class TransaccionService { // Clase de servicio para manejar la lógica de creación, listado

    private final TransaccionRepository transaccionRepository; // Repositorio para operaciones de base de datos sobre transacciones.
    private final CategoriaRepository categoriaRepository; // Repositorio para buscar categorías existentes.
    private final UsuarioRepository usuarioRepository; // Repositorio para buscar usuarios existentes.

    public Transaccion crearTransaccion(Transaccion transaccion) { // Método para crear una nueva transacción.

        if (transaccion.getUsuario() == null || transaccion.getUsuario().getId() == null) { // Valida que la transacción traiga usuario y que ese usuario tenga id.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La transacción debe tener un usuario válido."); // Si no cumple, responde error 400.
        }

        if (transaccion.getCategoria() == null || transaccion.getCategoria().getId() == null) { // Valida que la transacción traiga categoría y que esa categoría tenga id.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La transacción debe tener una categoría válida."); // Si no cumple, responde error 400.
        }

        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) { // Valida que el monto no sea nulo y sea mayor que cero.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero."); // Si no cumple, responde error 400.
        }
        
        // la transacción debe tener un usuario válido, una categoría válida, y un monto mayor a cero para poder ser creada. Si no cumple alguna de estas condiciones, se responde con un error 400 Bad Request y un mensaje explicativo.
        Usuario usuario = usuarioRepository.findById(transaccion.getUsuario().getId()) // Busca en la base de datos el usuario con el id recibido, para validar que exista y cargar su información completa.
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.")); // Si no existe, responde error 404.

        Categoria categoria = categoriaRepository.findById(transaccion.getCategoria().getId()) // Busca en la base de datos la categoría con el id recibido.
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada.")); 

        if (categoria.getUsuario() == null || categoria.getUsuario().getId() == null) { // Valida que la categoría encontrada sea personalizada y tenga usuario asociado.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La transacción solo puede usar categorías personalizadas del usuario."); // Si es global o no tiene usuario, responde error 400.
        }
        // cada transacción debe estar asociada a una categoría personalizada que pertenezca al mismo usuario de la transacción. No se permiten categorías globales ni categorías de otros usuarios.
        if (!categoria.getUsuario().getId().equals(usuario.getId())) { // Valida que la categoría realmente pertenezca al mismo usuario de la transacción.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "La categoría no pertenece al usuario de la transacción."); // Si pertenece a otro usuario, responde error 403.
        }

        transaccion.setUsuario(usuario); // Reemplaza el usuario que vino en el request por el usuario real cargado desde la base de datos.
        transaccion.setCategoria(categoria); // Reemplaza la categoría que vino en el request por la categoría real cargada desde la base de datos.

        if (transaccion.getFecha() == null) { // Verifica si la fecha vino vacía en el request.
            transaccion.setFecha(LocalDateTime.now()); // Si vino vacía, asigna automáticamente la fecha y hora actual del sistema.
        }

        return transaccionRepository.save(transaccion); // Guarda la transacción en la base de datos y la devuelve ya persistida.
    }

    public List<Transaccion> obtenerTransaccionesUsuario(Long idUsuario) { // Método para listar todas las transacciones de un usuario.

        if (idUsuario == null) { // Valida que el id del usuario no sea nulo.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del usuario no puede ser nulo."); // Si es nulo, responde error 400.
        }

        if (!usuarioRepository.existsById(idUsuario)) { // Verifica si el usuario existe en la base de datos.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."); // Si no existe, responde error 404.
        }

        return transaccionRepository.findByUsuarioId(idUsuario); // Busca y devuelve todas las transacciones que pertenezcan a ese usuario.
    }

    public void eliminarTransaccion(Long idTransaccion, Long idUsuario) { // Método para eliminar una transacción de un usuario específico.

        Transaccion transaccion = transaccionRepository.findByIdAndUsuarioId(idTransaccion, idUsuario) // Busca la transacción por su id y además valida que pertenezca a ese usuario.
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "Transacción no encontrada para ese usuario.")); // Si no existe o no pertenece a ese usuario, responde error 404.

        transaccionRepository.delete(transaccion); // Elimina la transacción encontrada de la base de datos.
    }
}
