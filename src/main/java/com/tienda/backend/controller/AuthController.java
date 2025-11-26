// controller/AuthController.java
package com.tienda.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.dto.LoginResponse;
import com.tienda.backend.dto.RegisterRequest;
import com.tienda.backend.dto.UserDTO;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.service.UsuarioService;
import com.tienda.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> userOpt = usuarioService.login(request.getEmail(), request.getPassword());
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (user.getRole() == null || user.getRole().trim().isEmpty()) {
                user.setRole("USER");
            }
            String token = jwtUtil.generateToken(user);
            UserDTO userDto = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
            LoginResponse resp = new LoginResponse(token, user.getRole(), userDto);
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(401).body("Email o contraseña incorrectos");
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            Usuario usuario = new Usuario();
            usuario.setName(req.getName());
            usuario.setEmail(req.getEmail());
            usuario.setPhone(req.getPhone());
            usuario.setPassword(req.getPassword());
            // Force role to USER regardless of client input
            usuario.setRole("USER");

            Usuario saved = usuarioService.save(usuario);
            UserDTO userDto = new UserDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getPhone(), saved.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    
}
