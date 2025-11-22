package com.tienda.backend.service;

import com.tienda.backend.model.Proveedor;
import com.tienda.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Crear proveedor (validando email único)
    public Proveedor createProveedor(Proveedor proveedor) {
        // Validar que el email no exista
        if (proveedorRepository.existsByEmail(proveedor.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        return proveedorRepository.save(proveedor);
    }

    // Obtener todos los proveedores (ordenados por id)
    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    // Obtener proveedor por ID
    public Optional<Proveedor> getProveedorById(Long id) {
        return proveedorRepository.findById(id);
    }

    // Obtener proveedor por email
    public Optional<Proveedor> getProveedorByEmail(String email) {
        return proveedorRepository.findByEmail(email);
    }

    // Actualizar proveedor
    public Proveedor updateProveedor(Long id, Proveedor proveedorActualizado) {
        return proveedorRepository.findById(id).map(proveedor -> {
            // Validar email único solo si cambió
            if (!proveedor.getEmail().equals(proveedorActualizado.getEmail()) 
                && proveedorRepository.existsByEmail(proveedorActualizado.getEmail())) {
                throw new RuntimeException("El correo ya está registrado");
            }
            
            proveedor.setName(proveedorActualizado.getName());
            proveedor.setRut(proveedorActualizado.getRut());
            proveedor.setEmail(proveedorActualizado.getEmail());
            proveedor.setPhone(proveedorActualizado.getPhone());
            proveedor.setDireccion(proveedorActualizado.getDireccion());
            
            return proveedorRepository.save(proveedor);
        }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
    }

    // Eliminar proveedor
    public void deleteProveedor(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        }
        proveedorRepository.deleteById(id);
    }
}
