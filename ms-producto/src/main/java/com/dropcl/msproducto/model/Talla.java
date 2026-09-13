package com.dropcl.msproducto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tallas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La talla es obligatoria")
    @Column(nullable = false)
    private String valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_talla", nullable = false)
    private TipoTalla tipoTalla;

    @Column(nullable = false)
    private Boolean disponible;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @ToString.Exclude
    private Producto producto;

    @PrePersist
    public void prePersist() {
        this.disponible = true;
    }

    public enum TipoTalla {
        CALZADO_US, CALZADO_EU, ROPA_ALPHA, ROPA_NUMERIC
    }
}