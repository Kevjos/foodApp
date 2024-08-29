package com.ror.foodapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "dishes")
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del plato no puede estar vacío")
    private String name;

    @Min(value = 1, message = "La cantidad debe ser un número mayor a cero.")
    private Integer portions;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero.")
    private Double price;
}
