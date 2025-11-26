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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.model.DetalleCompra;
import com.tienda.backend.service.DetalleCompraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/detalle-compras")
@CrossOrigin(origins = "*")
@Tag(name = "DetalleCompras", description = "API para gestión de los detalles de compras")
public class DetalleCompraController {

    @Autowired
    private DetalleCompraService detalleService;

    @PostMapping
    @Operation(summary = "Crear detalle de compra")
    public ResponseEntity<?> createDetalle(@RequestBody DetalleCompra detalle) {
        try {
            DetalleCompra creado = detalleService.createDetalle(detalle);
            Map<String, Object> resp = new HashMap<>();
            resp.put("id", creado.getId());
            resp.put("detalle", creado);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    @GetMapping
    @Operation(summary = "Listar detalles o filtrar por compraId")
    public ResponseEntity<List<DetalleCompra>> getAll(@RequestParam(required = false) Long compraId) {
        if (compraId == null) {
            return ResponseEntity.ok(detalleService.getAllDetalles());
        }
        return ResponseEntity.ok(detalleService.getDetallesByCompraId(compraId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle por ID")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<DetalleCompra> d = detalleService.getDetalleById(id);
        if (d.isPresent()) return ResponseEntity.ok(d.get());
        Map<String, String> err = new HashMap<>();
        err.put("error", "Detalle no encontrado con id: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle por ID")
    public ResponseEntity<?> updateDetalle(@PathVariable Long id, @RequestBody DetalleCompra detalle) {
        try {
            DetalleCompra actualizado = detalleService.updateDetalle(id, detalle);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            HttpStatus status = e.getMessage().contains("no encontrado") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(err);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle por ID")
    public ResponseEntity<?> deleteDetalle(@PathVariable Long id) {
        try {
            detalleService.deleteDetalle(id);
            Map<String, String> resp = new HashMap<>();
            resp.put("message", "Detalle eliminado exitosamente");
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
    }
}
