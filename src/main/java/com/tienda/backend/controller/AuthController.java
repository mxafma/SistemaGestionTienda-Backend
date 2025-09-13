// controller/AuthController.java
package com.tienda.backend.controller;

import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> user = usuarioService.login(request.getUsername(), request.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }
    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
    return usuarioService.save(usuario); 
    }

    
}
