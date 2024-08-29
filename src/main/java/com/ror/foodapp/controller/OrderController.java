package com.ror.foodapp.controller;

import com.ror.foodapp.model.Dish;
import com.ror.foodapp.model.Order;
import com.ror.foodapp.repository.DishRepository;
import com.ror.foodapp.repository.OrderRepository;
import com.ror.foodapp.service.EmailService;
import com.ror.foodapp.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private DishRepository dishRepository;

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        List<Dish> dishes = dishRepository.findAll();
        model.addAttribute("dishes", dishes);
        model.addAttribute("order", new Order());
        return "order-form";
    }


}
