package com.dropcl.msproducto.service;

import com.dropcl.msproducto.dto.*;
import com.dropcl.msproducto.model.*;
import java.util.List;

public interface ProductoService {

    
    Categoria crearCategoria(CrearCategoriaDTO dto);
    List<Categoria> obtenerCategorias();
    Categoria obtenerCategoriaPorId(Long id);


    Producto crearProducto(CrearProductoDTO dto);
    Producto obtenerProductoPorId(Long id);
    List<Producto> obtenerTodos();
    List<Producto> obtenerPorCategoria(Long categoriaId);
    List<Producto> obtenerPorMarca(String marca);
    List<Producto> buscarPorNombre(String nombre);
    Producto actualizarProducto(Long id, ActualizarProductoDTO dto);
    Producto cambiarEstado(Long id, String estado);


    Talla agregarTalla(CrearTallaDTO dto);
    List<Talla> obtenerTallasPorProducto(Long productoId);
    List<Talla> obtenerTallasDisponibles(Long productoId);
    void eliminarTalla(Long tallaId);
}