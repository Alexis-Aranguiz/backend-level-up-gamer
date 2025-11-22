package levelup.backend.controller;

import levelup.backend.dto.AuthResponse;
import levelup.backend.dto.LoginRequest;
import levelup.backend.dto.RegisterRequest;
import levelup.backend.entity.Rol;
import levelup.backend.entity.Usuario;
import levelup.backend.repository.RolRepository;
import levelup.backend.repository.UsuarioRepository;
import levelup.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ========= REGISTRO NORMAL (ROLE_USER) =========
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_USER");
                    return rolRepository.save(nuevoRol);
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.getRoles().add(rolUser);
        usuario.setPuntos(0);
        usuario.setDescuentoPct(0);

        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRoleNames()
        );

        AuthResponse response = new AuthResponse(
                token,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRoleNames(),
                usuario.getPuntos(),
                usuario.getDescuentoPct()
        );

        return ResponseEntity.ok(response);
    }

    // ========= REGISTRO ADMIN (ROLE_ADMIN) =========
    // Solo para desarrollo: NO uses esto abierto en producción
    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_ADMIN");
                    return rolRepository.save(nuevoRol);
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.getRoles().add(rolAdmin);
        usuario.setPuntos(0);
        usuario.setDescuentoPct(0);

        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRoleNames()
        );

        AuthResponse response = new AuthResponse(
                token,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRoleNames(),
                usuario.getPuntos(),
                usuario.getDescuentoPct()
        );

        return ResponseEntity.ok(response);
    }

    // ========= LOGIN =========
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRoleNames()
        );

        AuthResponse response = new AuthResponse(
                token,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRoleNames(),
                usuario.getPuntos(),
                usuario.getDescuentoPct()
        );

        return ResponseEntity.ok(response);
    }
}
