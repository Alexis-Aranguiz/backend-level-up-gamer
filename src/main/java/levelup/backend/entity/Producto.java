package levelup.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "productos")  // usa el nombre exacto de tu tabla
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 100)
    private String categoria;

    // Si en tu BD tienes más columnas (puntos, descuento, etc.)
    // las puedes agregar aquí con sus @Column correspondientes.
}
