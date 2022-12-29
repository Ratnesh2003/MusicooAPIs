package com.musicoo.apis.controller;

import com.musicoo.apis.service.Implementation.HomepageServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@RequiredArgsConstructor
@Controller
public class Homepage {

    private final HomepageServiceImpl service;
    public ResponseEntity<?> quickPicks(HttpServletRequest httpRequest) {
        return service.quickPicks(httpRequest);

    }

}
