package levelup.backend.service;

import levelup.backend.entity.Producto;
import levelup.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        // Valores por defecto si no vienen en el request
        if (producto.getPuntosBase() == null) {
            producto.setPuntosBase(0);
        }
        if (producto.getDescuentoPct() == null) {
            producto.setDescuentoPct(0);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizar(Long id, Producto producto) {
        return productoRepository.findById(id)
                .map(existente -> {
                    existente.setNombre(producto.getNombre());
                    existente.setDescripcion(producto.getDescripcion());
                    existente.setPrecio(producto.getPrecio());
                    existente.setStock(producto.getStock());
                    existente.setCategoria(producto.getCategoria());
                    existente.setPuntosBase(producto.getPuntosBase());
                    existente.setDescuentoPct(producto.getDescuentoPct());
                    return productoRepository.save(existente);
                })
                .orElseThrow(() ->
                        new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto con id " + id + " no existe");
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
}