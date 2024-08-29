package com.ror.foodapp.repository;


import com.ror.foodapp.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
