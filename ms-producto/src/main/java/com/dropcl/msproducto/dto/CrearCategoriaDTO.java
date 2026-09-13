package com.dropcl.msproducto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearCategoriaDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;
}