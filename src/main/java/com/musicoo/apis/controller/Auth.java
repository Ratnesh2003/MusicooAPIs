package com.musicoo.apis.controller;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.service.Implementation.UserAuthServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Auth {
    private UserAuthServiceImpl service;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@RequestBody MusicooUser musicooUser, HttpServletRequest httpRequest) throws MessagingException {
        return service.registerUser(musicooUser, httpRequest);
    }

    @RequestMapping(value = "/auth/confirm", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return service.confirmUserAccount(confirmationToken);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok().body("This is a test API");
    }
}
