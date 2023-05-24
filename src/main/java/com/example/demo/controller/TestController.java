package com.example.demo.controller;

import com.example.demo.service.FakeUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FakeUserDetailService userDetailService;

    @GetMapping("/api/v1/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("HELLO");
    }

    @GetMapping("/api/v1/secure")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok().body(userDetailService.getCurrentId()+"_"+userDetailService.getScopes());
    }

}
