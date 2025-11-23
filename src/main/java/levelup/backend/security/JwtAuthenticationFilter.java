package levelup.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // ⭐ CRÍTICO: Extraer y LIMPIAR el token (eliminar espacios)
            token = authHeader.substring(7).trim().replaceAll("\\s+", "");
            
            System.out.println("🔍 Token limpio (primeros 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");
            
            try {
                username = jwtUtil.getUsernameFromToken(token);
                System.out.println("✅ Username extraído: " + username);
            } catch (Exception e) {
                System.err.println("❌ Error extrayendo username: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtUtil.isTokenValid(token, userDetails)) {
                    // Extraer roles del JWT
                    List<String> rolesFromToken = jwtUtil.getRolesFromToken(token);
                    System.out.println("✅ Roles del JWT: " + rolesFromToken);
                    
                    // Convertir a GrantedAuthority
                    List<GrantedAuthority> authorities = rolesFromToken.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Crear autenticación
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    authorities
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ Autenticación exitosa con roles: " + authorities);
                }
            } catch (Exception e) {
                System.err.println("❌ Error en autenticación: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}