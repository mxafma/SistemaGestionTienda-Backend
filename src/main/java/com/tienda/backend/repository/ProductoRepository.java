package com.tienda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.backend.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByNombre(String nombre);
    
    Optional<Producto> findBySku(String sku);
    
    boolean existsByNombre(String nombre);
    
    boolean existsBySku(String sku);
    
    // Búsqueda por texto en nombre o categoría
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Producto> searchProductos(@Param("query") String query);
}
