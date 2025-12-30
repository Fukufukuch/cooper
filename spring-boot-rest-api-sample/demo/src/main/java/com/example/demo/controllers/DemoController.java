package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/demodemo")
    public Map<String, String> getDemo() {
        return Map.of("msg", "こんにちは。");
    }
}

