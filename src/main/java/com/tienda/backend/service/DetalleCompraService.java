package com.tienda.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.backend.model.DetalleCompra;
import com.tienda.backend.repository.DetalleCompraRepository;

@Service
public class DetalleCompraService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    public DetalleCompra createDetalle(DetalleCompra detalle) {
        return detalleCompraRepository.save(detalle);
    }

    public List<DetalleCompra> getAllDetalles() {
        return detalleCompraRepository.findAll();
    }

    public Optional<DetalleCompra> getDetalleById(Long id) {
        return detalleCompraRepository.findById(id);
    }

    public List<DetalleCompra> getDetallesByCompraId(Long compraId) {
        return detalleCompraRepository.findByCompraId(compraId);
    }

    public DetalleCompra updateDetalle(Long id, DetalleCompra detalle) {
        Optional<DetalleCompra> existente = detalleCompraRepository.findById(id);
        if (existente.isEmpty()) {
            throw new RuntimeException("Detalle no encontrado con id: " + id);
        }
        DetalleCompra actual = existente.get();
        if (detalle.getProducto() != null) actual.setProducto(detalle.getProducto());
        if (detalle.getCantidad() != null) actual.setCantidad(detalle.getCantidad());
        if (detalle.getPrecioUnitario() != null) actual.setPrecioUnitario(detalle.getPrecioUnitario());
        if (detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
            actual.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
        }
        return detalleCompraRepository.save(actual);
    }

    public void deleteDetalle(Long id) {
        if (!detalleCompraRepository.existsById(id)) {
            throw new RuntimeException("Detalle no encontrado con id: " + id);
        }
        detalleCompraRepository.deleteById(id);
    }
}
