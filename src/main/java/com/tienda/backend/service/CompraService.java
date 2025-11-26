package com.tienda.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.backend.model.Compra;
import com.tienda.backend.model.DetalleCompra;
import com.tienda.backend.repository.CompraRepository;

@Service
@Transactional
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    /**
     * Guarda una compra junto con sus detalles. Calcula subtotal por detalle y total.
     */
    public Compra createCompra(Compra compra) {
        if (compra.getProveedor() == null || compra.getProveedor().trim().isEmpty()) {
            throw new RuntimeException("El proveedor es obligatorio");
        }

        // Fecha por defecto
        if (compra.getFecha() == null) {
            compra.setFecha(LocalDate.now());
        }

        double total = 0.0;

        if (compra.getDetalles() != null && !compra.getDetalles().isEmpty()) {
            for (DetalleCompra d : compra.getDetalles()) {
                if (d.getCantidad() == null || d.getCantidad() <= 0) {
                    throw new RuntimeException("La cantidad debe ser mayor a 0 para el producto: " + d.getProducto());
                }
                if (d.getPrecioUnitario() == null || d.getPrecioUnitario() < 0) {
                    throw new RuntimeException("El precio unitario debe ser >= 0 para el producto: " + d.getProducto());
                }
                double subtotal = d.getCantidad() * d.getPrecioUnitario();
                d.setSubtotal(subtotal);
                d.setCompra(compra);
                total += subtotal;
            }
        }

        if (total < 0) {
            throw new RuntimeException("Total inválido");
        }

        compra.setTotal(total);

        return compraRepository.save(compra);
    }

    @Transactional(readOnly = true)
    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Compra> getCompraById(Long id) {
        return compraRepository.findById(id);
    }

    public Compra updateCompra(Long id, Compra compra) {
        Optional<Compra> existente = compraRepository.findById(id);
        if (existente.isEmpty()) {
            throw new RuntimeException("Compra no encontrada con id: " + id);
        }

        Compra actual = existente.get();

        if (compra.getProveedor() != null) {
            actual.setProveedor(compra.getProveedor());
        }
        if (compra.getFormaPago() != null) {
            actual.setFormaPago(compra.getFormaPago());
        }
        if (compra.getFecha() != null) {
            actual.setFecha(compra.getFecha());
        }

        // Reemplazar detalles si se envían
        if (compra.getDetalles() != null) {
            actual.getDetalles().clear();
            double total = 0.0;
            for (DetalleCompra d : compra.getDetalles()) {
                if (d.getCantidad() == null || d.getCantidad() <= 0) {
                    throw new RuntimeException("La cantidad debe ser mayor a 0 para el producto: " + d.getProducto());
                }
                if (d.getPrecioUnitario() == null || d.getPrecioUnitario() < 0) {
                    throw new RuntimeException("El precio unitario debe ser >= 0 para el producto: " + d.getProducto());
                }
                double subtotal = d.getCantidad() * d.getPrecioUnitario();
                d.setSubtotal(subtotal);
                d.setCompra(actual);
                actual.getDetalles().add(d);
                total += subtotal;
            }
            actual.setTotal(total);
        }

        return compraRepository.save(actual);
    }

    public void deleteCompra(Long id) {
        if (!compraRepository.existsById(id)) {
            throw new RuntimeException("Compra no encontrada con id: " + id);
        }
        compraRepository.deleteById(id);
    }
}
