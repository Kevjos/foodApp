package com.ror.foodapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @GetMapping("/")
    public RedirectView redirectToOrderForm() {
        return new RedirectView("/orders/new");
    }
}

