package com.tienda.levelup.service;

import com.tienda.levelup.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    List<Producto> listarTodos();

    Optional<Producto> buscarPorId(Long id);

    Producto guardar(Producto producto);

    Producto actualizar(Long id, Producto producto);

    void eliminar(Long id);

    List<Producto> listarPorCategoria(String categoria);
}
