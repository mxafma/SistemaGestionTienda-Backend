package com.tienda.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.backend.model.Categoria;
import com.tienda.backend.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Crear nueva categoría con validaciones
     * - nombre.trim().length >= 3
     * - nombre único
     */
    public Categoria createCategoria(Categoria categoria) {
        // Validar que el nombre no sea nulo o vacío
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría es obligatorio");
        }

        // Limpiar y validar longitud del nombre
        String nombreTrimmed = categoria.getNombre().trim();
        if (nombreTrimmed.length() < 3) {
            throw new RuntimeException("El nombre de la categoría debe tener al menos 3 caracteres");
        }

        // Validar que el nombre sea único (case-insensitive)
        if (categoriaRepository.existsByNombre(nombreTrimmed)) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + nombreTrimmed);
        }

        // Guardar con nombre limpio
        categoria.setNombre(nombreTrimmed);
        if (categoria.getDescripcion() != null) {
            categoria.setDescripcion(categoria.getDescripcion().trim());
        }

        return categoriaRepository.save(categoria);
    }

    /**
     * Obtener todas las categorías
     */
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Obtener lista de nombres de categorías (únicos y ordenados)
     * Para autocompletar en el frontend
     */
    public List<String> getCategoriasNombres() {
        return categoriaRepository.findAll()
                .stream()
                .map(Categoria::getNombre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Obtener categoría por ID
     */
    public Optional<Categoria> getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Obtener categoría por nombre
     */
    public Optional<Categoria> getCategoriaByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    /**
     * Actualizar categoría existente
     */
    public Categoria updateCategoria(Long id, Categoria categoria) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        
        if (categoriaExistente.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }

        Categoria categoriaActualizada = categoriaExistente.get();

        // Si se actualiza el nombre, validar
        if (categoria.getNombre() != null) {
            String nombreTrimmed = categoria.getNombre().trim();
            
            if (nombreTrimmed.length() < 3) {
                throw new RuntimeException("El nombre de la categoría debe tener al menos 3 caracteres");
            }

            // Verificar que el nombre no esté en uso por otra categoría
            Optional<Categoria> otraCategoria = categoriaRepository.findByNombre(nombreTrimmed);
            if (otraCategoria.isPresent() && !otraCategoria.get().getId().equals(id)) {
                throw new RuntimeException("Ya existe otra categoría con el nombre: " + nombreTrimmed);
            }

            categoriaActualizada.setNombre(nombreTrimmed);
        }

        // Actualizar descripción
        if (categoria.getDescripcion() != null) {
            categoriaActualizada.setDescripcion(categoria.getDescripcion().trim());
        }

        return categoriaRepository.save(categoriaActualizada);
    }

    /**
     * Eliminar categoría
     */
    public void deleteCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
