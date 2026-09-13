package com.dropcl.msproducto.service.impl;

import com.dropcl.msproducto.dto.*;
import com.dropcl.msproducto.model.*;
import com.dropcl.msproducto.repository.*;
import com.dropcl.msproducto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final TallaRepository tallaRepository;



    @Override
    public Categoria crearCategoria(CrearCategoriaDTO dto) {
        log.info("Creando categoría: {}", dto.getNombre());

        // Regla de negocio: nombre de categoría único
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            log.warn("Categoría ya existe: {}", dto.getNombre());
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();

        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría creada con id: {}", guardada.getId());
        return guardada;
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        log.info("Obteniendo todas las categorías activas");
        return categoriaRepository.findByActivoTrue();
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        log.info("Buscando categoría con id: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Categoría no encontrada con id: {}", id);
                    return new RuntimeException("Categoría no encontrada con id: " + id);
                });
    }



    @Override
    public Producto crearProducto(CrearProductoDTO dto) {
        log.info("Creando producto: {}", dto.getNombre());

        // Regla de negocio: la categoría debe existir
        Categoria categoria = obtenerCategoriaPorId(dto.getCategoriaId());

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .marca(dto.getMarca())
                .descripcion(dto.getDescripcion())
                .precioBase(dto.getPrecioBase())
                .imagenUrl(dto.getImagenUrl())
                .modelo(dto.getModelo())
                .categoria(categoria)
                .build();

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado con id: {}", guardado.getId());
        return guardado;
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con id: {}", id);
                    return new RuntimeException("Producto no encontrado con id: " + id);
                });
    }

    @Override
    public List<Producto> obtenerTodos() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> obtenerPorCategoria(Long categoriaId) {
        log.info("Obteniendo productos de categoría id: {}", categoriaId);
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Producto> obtenerPorMarca(String marca) {
        log.info("Obteniendo productos de marca: {}", marca);
        return productoRepository.findByMarca(marca);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        log.info("Buscando productos con nombre: {}", nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Producto actualizarProducto(Long id, ActualizarProductoDTO dto) {
        log.info("Actualizando producto con id: {}", id);

        Producto producto = obtenerProductoPorId(id);

        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getMarca() != null) producto.setMarca(dto.getMarca());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecioBase() != null) producto.setPrecioBase(dto.getPrecioBase());
        if (dto.getImagenUrl() != null) producto.setImagenUrl(dto.getImagenUrl());
        if (dto.getModelo() != null) producto.setModelo(dto.getModelo());
        if (dto.getCategoriaId() != null) {
            Categoria categoria = obtenerCategoriaPorId(dto.getCategoriaId());
            producto.setCategoria(categoria);
        }

        Producto actualizado = productoRepository.save(producto);
        log.info("Producto actualizado correctamente con id: {}", actualizado.getId());
        return actualizado;
    }

    @Override
    public Producto cambiarEstado(Long id, String estado) {
        log.info("Cambiando estado del producto id: {} a {}", id, estado);

        Producto producto = obtenerProductoPorId(id);

        try {
            producto.setEstado(Producto.EstadoProducto.valueOf(estado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Estado inválido: {}", estado);
            throw new RuntimeException("Estado inválido: " + estado);
        }

        Producto actualizado = productoRepository.save(producto);
        log.info("Estado del producto id: {} cambiado a {}", id, estado);
        return actualizado;
    }



    @Override
    public Talla agregarTalla(CrearTallaDTO dto) {
        log.info("Agregando talla {} al producto id: {}", dto.getValor(), dto.getProductoId());

        Producto producto = obtenerProductoPorId(dto.getProductoId());

        // Regla de negocio: no puede haber tallas duplicadas en el mismo producto
        if (tallaRepository.existsByProductoIdAndValor(dto.getProductoId(), dto.getValor())) {
            log.warn("Talla {} ya existe para producto id: {}", dto.getValor(), dto.getProductoId());
            throw new RuntimeException("Esta talla ya existe para este producto");
        }

        Talla talla = Talla.builder()
                .valor(dto.getValor())
                .tipoTalla(Talla.TipoTalla.valueOf(dto.getTipoTalla()))
                .producto(producto)
                .build();

        Talla guardada = tallaRepository.save(talla);
        log.info("Talla agregada con id: {}", guardada.getId());
        return guardada;
    }

    @Override
    public List<Talla> obtenerTallasPorProducto(Long productoId) {
        log.info("Obteniendo tallas del producto id: {}", productoId);
        return tallaRepository.findByProductoId(productoId);
    }

    @Override
    public List<Talla> obtenerTallasDisponibles(Long productoId) {
        log.info("Obteniendo tallas disponibles del producto id: {}", productoId);
        return tallaRepository.findByProductoIdAndDisponibleTrue(productoId);
    }

    @Override
    public void eliminarTalla(Long tallaId) {
        log.info("Eliminando talla con id: {}", tallaId);
        if (!tallaRepository.existsById(tallaId)) {
            log.warn("Talla no encontrada con id: {}", tallaId);
            throw new RuntimeException("Talla no encontrada con id: " + tallaId);
        }
        tallaRepository.deleteById(tallaId);
        log.info("Talla eliminada correctamente con id: {}", tallaId);
    }
}