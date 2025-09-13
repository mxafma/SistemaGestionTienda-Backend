// service/UsuarioService.java
package com.tienda.backend.service;

import com.tienda.backend.model.Usuario;
import com.tienda.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> login(String username, String password) {
        return usuarioRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


}
