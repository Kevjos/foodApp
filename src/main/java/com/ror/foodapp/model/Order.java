package com.ror.foodapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish; // Relación con Dish

    private int quantity;
    private Double amount;
    private LocalDate orderDate;
    private boolean delivered;
    private String stripeSessionId;

    // Getters y Setters
}
