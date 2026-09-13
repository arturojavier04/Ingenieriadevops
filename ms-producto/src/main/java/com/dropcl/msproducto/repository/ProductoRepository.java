package com.dropcl.msproducto.repository;

import com.dropcl.msproducto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByMarca(String marca);
    List<Producto> findByEstado(Producto.EstadoProducto estado);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}