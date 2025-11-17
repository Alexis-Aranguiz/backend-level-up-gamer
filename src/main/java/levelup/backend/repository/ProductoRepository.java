package com.tienda.levelup.repository;

import com.tienda.levelup.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Ejemplo de método extra: buscar por categoría
    List<Producto> findByCategoria(String categoria);
}
