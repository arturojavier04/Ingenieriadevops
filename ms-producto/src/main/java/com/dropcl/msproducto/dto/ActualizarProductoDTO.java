package com.dropcl.msproducto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ActualizarProductoDTO {

    @Size(min = 2, max = 200)
    private String nombre;

    @Size(min = 2, max = 100)
    private String marca;

    @Size(max = 500)
    private String descripcion;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioBase;

    @Size(max = 500)
    private String imagenUrl;

    private String modelo;
    private Long categoriaId;
    private String estado;
}