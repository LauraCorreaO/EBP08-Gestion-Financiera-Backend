package com.ebp08.gestion_financiera_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Busca el header Authorization en la petición
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay token o no empieza con "Bearer ", deja pasar la petición
        //    (rutas públicas como /login y /registro no tienen token)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrae el token quitando el prefijo "Bearer "
        String token = authHeader.substring(7);

        // 4. Valida el token y si es válido, autentica al usuario en Spring Security
        if (jwtUtil.esValido(token)) {
            String correo = jwtUtil.extraerCorreo(token);

            // Le dice a Spring Security quién es el usuario y que ya está autenticado
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            correo,          // principal (identificador del usuario)
                            null,           // credentials (no necesitamos la contraseña aquí)
                            List.of());     // authorities (roles o permisos, aquí vacío)

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. Deja continuar la petición hacia el controller
        filterChain.doFilter(request, response);
    }
}