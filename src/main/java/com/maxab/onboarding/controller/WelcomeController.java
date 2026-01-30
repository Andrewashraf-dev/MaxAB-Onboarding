package com.maxab.onboarding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/success")
    public String success() {
        return "success.html";
    }
}