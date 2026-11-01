
package com.dropcl.msproducto.controller;

import com.dropcl.msproducto.dto.CrearCategoriaDTO;
import com.dropcl.msproducto.dto.CrearProductoDTO;
import com.dropcl.msproducto.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    private ProductoService productoService;
    private ProductoController controller;

    @BeforeEach
    void configurar() {
        productoService = mock(ProductoService.class);
        controller = new ProductoController(productoService);
    }

    @Test
    void obtenerTodosDevuelve200() {
        when(productoService.obtenerTodos())
                .thenReturn(Collections.emptyList());

        var respuesta = controller.obtenerTodos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().isEmpty());

        verify(productoService).obtenerTodos();
    }

    @Test
    void obtenerCategoriasDevuelve200() {
        when(productoService.obtenerCategorias())
                .thenReturn(Collections.emptyList());

        var respuesta = controller.obtenerCategorias();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().isEmpty());

        verify(productoService).obtenerCategorias();
    }

    @Test
    void productoNoEncontradoDevuelve404() {
        when(productoService.obtenerProductoPorId(999L))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        var respuesta = controller.obtenerPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("Producto no encontrado", respuesta.getBody());
    }

    @Test
    void categoriaDuplicadaDevuelve409() {
        CrearCategoriaDTO dto = mock(CrearCategoriaDTO.class);

        when(productoService.crearCategoria(dto))
                .thenThrow(new RuntimeException("Categoria duplicada"));

        var respuesta = controller.crearCategoria(dto);

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("Categoria duplicada", respuesta.getBody());
    }

    @Test
    void errorAlCrearProductoDevuelve400() {
        CrearProductoDTO dto = mock(CrearProductoDTO.class);

        when(productoService.crearProducto(dto))
                .thenThrow(new RuntimeException("Error al crear producto"));

        var respuesta = controller.crearProducto(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }

    @Test
    void estadoInvalidoDevuelve400() {
        when(productoService.cambiarEstado(1L, "INVALIDO"))
                .thenThrow(new RuntimeException("Estado invalido"));

        var respuesta = controller.cambiarEstado(1L, "INVALIDO");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }

    @Test
    void eliminarTallaNoExistenteDevuelve404() {
        doThrow(new RuntimeException("Talla no encontrada"))
                .when(productoService).eliminarTalla(999L);

        var respuesta = controller.eliminarTalla(999L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }
}
