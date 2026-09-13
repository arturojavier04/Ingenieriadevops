package com.dropcl.msproducto.repository;

import com.dropcl.msproducto.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Long> {

    List<Talla> findByProductoId(Long productoId);
    List<Talla> findByProductoIdAndDisponibleTrue(Long productoId);
    boolean existsByProductoIdAndValor(Long productoId, String valor);
}