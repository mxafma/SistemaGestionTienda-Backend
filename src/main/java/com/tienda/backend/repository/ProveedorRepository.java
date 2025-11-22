package com.tienda.backend.repository;

import com.tienda.backend.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    // Método para buscar por email (getByEmailP)
    Optional<Proveedor> findByEmail(String email);
    
    // Método para verificar si existe un email
    boolean existsByEmail(String email);
}
