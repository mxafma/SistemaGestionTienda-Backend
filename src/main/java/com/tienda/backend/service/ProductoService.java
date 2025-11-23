package com.tienda.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Crear nuevo producto con validaciones:
     * - nombre.trim().length >= 4
     * - nombre único
     * - si sku no es null/vacío: solo dígitos y SKU único
     */
    public Producto createProducto(Producto producto) {
        // Validar que el nombre no sea nulo o vacío
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }

        // Limpiar y validar longitud del nombre
        String nombreTrimmed = producto.getNombre().trim();
        if (nombreTrimmed.length() < 4) {
            throw new RuntimeException("El nombre del producto debe tener al menos 4 caracteres");
        }

        // Validar que el nombre sea único
        if (productoRepository.existsByNombre(nombreTrimmed)) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + nombreTrimmed);
        }

        // Validar SKU si se proporciona
        if (producto.getSku() != null && !producto.getSku().trim().isEmpty()) {
            String skuTrimmed = producto.getSku().trim();
            
            // Validar que solo contenga dígitos
            if (!skuTrimmed.matches("\\d+")) {
                throw new RuntimeException("El SKU solo puede contener números");
            }

            // Validar que el SKU sea único
            if (productoRepository.existsBySku(skuTrimmed)) {
                throw new RuntimeException("Ya existe un producto con el SKU: " + skuTrimmed);
            }

            producto.setSku(skuTrimmed);
        } else {
            producto.setSku(null);
        }

        // Guardar con nombre limpio
        producto.setNombre(nombreTrimmed);
        
        // Limpiar otros campos opcionales
        if (producto.getPhotoUri() != null) {
            producto.setPhotoUri(producto.getPhotoUri().trim());
        }
        if (producto.getCategoria() != null) {
            producto.setCategoria(producto.getCategoria().trim());
        }

        return productoRepository.save(producto);
    }

    /**
     * Obtener todos los productos
     */
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    /**
     * Obtener producto por ID
     */
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Buscar productos por texto (nombre o categoría)
     */
    public List<Producto> searchProductos(String query) {
        if (query == null || query.trim().isEmpty()) {
            return productoRepository.findAll();
        }
        return productoRepository.searchProductos(query.trim());
    }

    /**
     * Actualizar producto existente
     */
    public Producto updateProducto(Long id, Producto producto) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        if (productoExistente.isEmpty()) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }

        Producto productoActualizado = productoExistente.get();

        // Si se actualiza el nombre, validar
        if (producto.getNombre() != null) {
            String nombreTrimmed = producto.getNombre().trim();
            
            if (nombreTrimmed.length() < 4) {
                throw new RuntimeException("El nombre del producto debe tener al menos 4 caracteres");
            }

            // Verificar que el nombre no esté en uso por otro producto
            Optional<Producto> otroProducto = productoRepository.findByNombre(nombreTrimmed);
            if (otroProducto.isPresent() && !otroProducto.get().getId().equals(id)) {
                throw new RuntimeException("Ya existe otro producto con el nombre: " + nombreTrimmed);
            }

            productoActualizado.setNombre(nombreTrimmed);
        }

        // Si se actualiza el SKU, validar
        if (producto.getSku() != null) {
            if (producto.getSku().trim().isEmpty()) {
                productoActualizado.setSku(null);
            } else {
                String skuTrimmed = producto.getSku().trim();
                
                // Validar que solo contenga dígitos
                if (!skuTrimmed.matches("\\d+")) {
                    throw new RuntimeException("El SKU solo puede contener números");
                }

                // Verificar que el SKU no esté en uso por otro producto
                Optional<Producto> otroProducto = productoRepository.findBySku(skuTrimmed);
                if (otroProducto.isPresent() && !otroProducto.get().getId().equals(id)) {
                    throw new RuntimeException("Ya existe otro producto con el SKU: " + skuTrimmed);
                }

                productoActualizado.setSku(skuTrimmed);
            }
        }

        // Actualizar otros campos opcionales
        if (producto.getPhotoUri() != null) {
            productoActualizado.setPhotoUri(producto.getPhotoUri().trim());
        }
        if (producto.getCategoria() != null) {
            productoActualizado.setCategoria(producto.getCategoria().trim());
        }

        return productoRepository.save(productoActualizado);
    }

    /**
     * Eliminar producto
     */
    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
}
