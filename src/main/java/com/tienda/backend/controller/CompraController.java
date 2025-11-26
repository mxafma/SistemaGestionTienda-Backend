package com.tienda.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.model.Compra;
import com.tienda.backend.service.CompraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
@Tag(name = "Compras", description = "API para gestión de compras y sus detalles")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    @Operation(summary = "Crear compra", description = "Crea una compra con sus detalles y calcula subtotales y total")
    public ResponseEntity<?> createCompra(@RequestBody Compra compra) {
        try {
            Compra nueva = compraService.createCompra(compra);
            Map<String, Object> resp = new HashMap<>();
            resp.put("id", nueva.getId());
            resp.put("message", "Compra creada exitosamente");
            resp.put("compra", nueva);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    @GetMapping
    @Operation(summary = "Listar compras", description = "Obtiene todas las compras")
    public ResponseEntity<List<Compra>> getAll() {
        return ResponseEntity.ok(compraService.getAllCompras());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener compra por ID")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Compra> c = compraService.getCompraById(id);
        if (c.isPresent()) return ResponseEntity.ok(c.get());
        Map<String, String> err = new HashMap<>();
        err.put("error", "Compra no encontrada con id: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar compra por ID")
    public ResponseEntity<?> updateCompra(@PathVariable Long id, @RequestBody Compra compra) {
        try {
            Compra actualizado = compraService.updateCompra(id, compra);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            HttpStatus status = e.getMessage().contains("no encontrada") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(err);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar compra por ID")
    public ResponseEntity<?> deleteCompra(@PathVariable Long id,
                                          @RequestHeader(value = "Authorization", required = false) String authorization) {
        // Validar header Authorization
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Authorization header missing or invalid. Use: Authorization: Bearer <token>");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        String token = authorization.substring(7).trim();
        // Validación mínima del token usando TokenService
        if (!com.tienda.backend.service.TokenService.isValid(token)) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Token inválido o expirado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        try {
            compraService.deleteCompra(id);
            Map<String, String> resp = new HashMap<>();
            resp.put("message", "Compra eliminada exitosamente");
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
    }
}
