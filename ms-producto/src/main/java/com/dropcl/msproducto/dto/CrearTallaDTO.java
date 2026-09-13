package com.dropcl.msproducto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearTallaDTO {

    @NotBlank(message = "El valor de la talla es obligatorio")
    private String valor;

    @NotBlank(message = "El tipo de talla es obligatorio")
    @Pattern(regexp = "CALZADO_US|CALZADO_EU|ROPA_ALPHA|ROPA_NUMERIC",
            message = "Tipo de talla inválido")
    private String tipoTalla;

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;
}