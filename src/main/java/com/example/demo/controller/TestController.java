package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/v1/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("HELLO");
    }

    @GetMapping("/api/v1/secure")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok().body("SECURE");
    }

}
