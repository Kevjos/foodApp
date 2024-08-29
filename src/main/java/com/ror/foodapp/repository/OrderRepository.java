package com.ror.foodapp.repository;

import com.ror.foodapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
