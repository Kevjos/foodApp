package com.ror.foodapp.controller;

import com.ror.foodapp.model.Dish;
import com.ror.foodapp.repository.DishRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dishes")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class DishController {

    @Autowired
    private DishRepository dishRepository;

    @GetMapping
    public String listDishes(Model model) {
        List<Dish> dishes = dishRepository.findAll();
        model.addAttribute("dishes", dishes);
        return "dish-list";
    }

    @GetMapping("/new")
    public String showDishForm(Model model) {
        model.addAttribute("dish", new Dish());
        return "dish-form";
    }

    @PostMapping
    public String saveDish(@Valid @ModelAttribute Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "dish-form";
        }

        dishRepository.save(dish);
        return "redirect:/dishes";
    }

    @GetMapping("/edit/{id}")
    public String showEditDishForm(@PathVariable("id") Long id, Model model) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + id));
        model.addAttribute("dish", dish);
        return "dish-form";
    }

    @PostMapping("/update/{id}")
    public String updateDish(@PathVariable("id") Long id, @ModelAttribute Dish dish) {
        if (dish.getId() != null && dishRepository.existsById(dish.getId())) {
            // Si el ID existe, actualiza el plato
            Dish existingDish = dishRepository.findById(dish.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + dish.getId()));
            existingDish.setName(dish.getName());
            existingDish.setPortions(dish.getPortions());
            existingDish.setPrice(dish.getPrice());
            dishRepository.save(existingDish);
        }
        return "redirect:/dishes";
    }

    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable("id") Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + id));
        dishRepository.delete(dish);
        return "redirect:/dishes";
    }
}

