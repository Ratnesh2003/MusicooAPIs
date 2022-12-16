package com.musicoo.apis.controller;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.service.Implementation.UserAuthServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Auth {
    private final UserAuthServiceImpl service;

//    public Auth (UserAuthServiceImpl userAuthService) {
//        this.service = userAuthService;
//    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@RequestBody MusicooUser musicooUser, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        return service.registerUser(musicooUser, httpRequest);
    }

    @RequestMapping(value = "/auth/confirm", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam String token, @RequestParam String email) throws ExecutionException {
        return service.confirmUserAccount(token, email);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok().body("This is a test API");
    }
}
