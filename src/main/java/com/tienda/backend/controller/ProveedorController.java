package com.tienda.backend.controller;

import com.tienda.backend.model.Proveedor;
import com.tienda.backend.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // POST /proveedores - Crear nuevo proveedor
    @PostMapping
    public ResponseEntity<?> createProveedor(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevoProveedor = proveedorService.createProveedor(proveedor);
            Map<String, Object> response = new HashMap<>();
            response.put("id", nuevoProveedor.getId());
            response.put("message", "Proveedor creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // GET /proveedores - Obtener todos los proveedores
    @GetMapping
    public ResponseEntity<List<Proveedor>> getAllProveedores() {
        List<Proveedor> proveedores = proveedorService.getAllProveedores();
        return ResponseEntity.ok(proveedores);
    }

    // GET /proveedores/{id} - Obtener proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProveedorById(@PathVariable Long id) {
        return proveedorService.getProveedorById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Proveedor no encontrado con id: " + id)));
    }

    // PUT /proveedores/{id} - Actualizar proveedor
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        try {
            Proveedor proveedorActualizado = proveedorService.updateProveedor(id, proveedor);
            return ResponseEntity.ok(proveedorActualizado);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // DELETE /proveedores/{id} - Eliminar proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProveedor(@PathVariable Long id) {
        try {
            proveedorService.deleteProveedor(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Proveedor eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
