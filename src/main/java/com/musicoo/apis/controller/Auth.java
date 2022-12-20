package com.musicoo.apis.controller;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.ConfirmOTPReq;
import com.musicoo.apis.payload.request.EmailReq;
import com.musicoo.apis.payload.request.LoginReq;
import com.musicoo.apis.payload.request.TokenRefreshReq;
import com.musicoo.apis.service.Implementation.UserAuthServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginReq loginReq) {
        return service.loginUser(loginReq);
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshReq tokenRefreshReq) {
        return service.generateAccessToken(tokenRefreshReq.getRefreshToken());
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailReq emailReq) throws MessagingException, ExecutionException {
        return service.forgotUserPassword(emailReq.getEmail());
    }

    @PostMapping("/auth/confirm-otp")
    public ResponseEntity<?> confirmOtp(@Valid @RequestBody ConfirmOTPReq confirmOTPReq) throws ExecutionException {
        return service.confirmUserOTP(confirmOTPReq);
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ConfirmOTPReq confirmOTPReq) throws ExecutionException {
        return service.changeUserPassword(confirmOTPReq);
    }

//    @PostMapping("/auth/google")
//    public ResponseEntity<?> googleLogin(@RequestBody TokenRefreshReq tokenRefreshReq) {
//        return service.googleLogin(tokenRefreshReq.getRefreshToken());
//    }

    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok().body("This is a test API");
    }
}
