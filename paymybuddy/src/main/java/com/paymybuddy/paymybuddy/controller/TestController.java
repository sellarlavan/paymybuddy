package com.paymybuddy.paymybuddy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String securedEndpoint() {
        return "Authentification réussie !";
    }
}
