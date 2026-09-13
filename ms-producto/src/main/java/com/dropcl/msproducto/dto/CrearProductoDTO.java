package com.dropcl.msproducto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearProductoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 200)
    private String nombre;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 2, max = 100)
    private String marca;

    @Size(max = 500)
    private String descripcion;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioBase;

    @Size(max = 500)
    private String imagenUrl;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El id de categoría es obligatorio")
    private Long categoriaId;
}