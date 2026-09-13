package com.dropcl.msproducto.controller;

import com.dropcl.msproducto.dto.*;
import com.dropcl.msproducto.model.*;
import com.dropcl.msproducto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
    private final ProductoService productoService;



    @PostMapping("/categorias")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CrearCategoriaDTO dto) {
        log.info("Petición para crear categoría: {}", dto.getNombre());
        try {
            Categoria categoria = productoService.crearCategoria(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (RuntimeException e) {
            log.error("Error al crear categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        log.info("Petición para obtener categorías");
        return ResponseEntity.ok(productoService.obtenerCategorias());
    }


    @GetMapping("/categorias/{id}")
    public ResponseEntity<?> obtenerCategoriaPorId(@PathVariable Long id) {
        log.info("Petición para obtener categoría con id: {}", id);
        try {
            return ResponseEntity.ok(productoService.obtenerCategoriaPorId(id));
        } catch (RuntimeException e) {
            log.error("Error al obtener categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody CrearProductoDTO dto) {
        log.info("Petición para crear producto: {}", dto.getNombre());
        try {
            Producto producto = productoService.crearProducto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (RuntimeException e) {
            log.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        log.info("Petición para obtener todos los productos");
        return ResponseEntity.ok(productoService.obtenerTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Petición para obtener producto con id: {}", id);
        try {
            return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
        } catch (RuntimeException e) {
            log.error("Error al obtener producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        log.info("Petición para obtener productos de categoría id: {}", categoriaId);
        return ResponseEntity.ok(productoService.obtenerPorCategoria(categoriaId));
    }


    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Producto>> obtenerPorMarca(@PathVariable String marca) {
        log.info("Petición para obtener productos de marca: {}", marca);
        return ResponseEntity.ok(productoService.obtenerPorMarca(marca));
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        log.info("Petición para buscar productos con nombre: {}", nombre);
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,
                                                @Valid @RequestBody ActualizarProductoDTO dto) {
        log.info("Petición para actualizar producto con id: {}", id);
        try {
            return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
        } catch (RuntimeException e) {
            log.error("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestParam String estado) {
        log.info("Petición para cambiar estado del producto id: {}", id);
        try {
            return ResponseEntity.ok(productoService.cambiarEstado(id, estado));
        } catch (RuntimeException e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/tallas")
    public ResponseEntity<?> agregarTalla(@Valid @RequestBody CrearTallaDTO dto) {
        log.info("Petición para agregar talla al producto id: {}", dto.getProductoId());
        try {
            Talla talla = productoService.agregarTalla(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(talla);
        } catch (RuntimeException e) {
            log.error("Error al agregar talla: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/{productoId}/tallas")
    public ResponseEntity<List<Talla>> obtenerTallas(@PathVariable Long productoId) {
        log.info("Petición para obtener tallas del producto id: {}", productoId);
        return ResponseEntity.ok(productoService.obtenerTallasPorProducto(productoId));
    }


    @GetMapping("/{productoId}/tallas/disponibles")
    public ResponseEntity<List<Talla>> obtenerTallasDisponibles(@PathVariable Long productoId) {
        log.info("Petición para obtener tallas disponibles del producto id: {}", productoId);
        return ResponseEntity.ok(productoService.obtenerTallasDisponibles(productoId));
    }


    @DeleteMapping("/tallas/{tallaId}")
    public ResponseEntity<?> eliminarTalla(@PathVariable Long tallaId) {
        log.info("Petición para eliminar talla con id: {}", tallaId);
        try {
            productoService.eliminarTalla(tallaId);
            return ResponseEntity.ok("Talla eliminada correctamente");
        } catch (RuntimeException e) {
            log.error("Error al eliminar talla: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}