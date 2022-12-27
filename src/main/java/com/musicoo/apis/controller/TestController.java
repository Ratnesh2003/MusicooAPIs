package com.musicoo.apis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/")
    public ResponseEntity<?> root() {
        return ResponseEntity.ok().body("This is home route.");
    }
}
