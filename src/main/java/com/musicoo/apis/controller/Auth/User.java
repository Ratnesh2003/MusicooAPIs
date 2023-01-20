package com.musicoo.apis.controller.Auth;

import com.musicoo.apis.payload.request.*;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.Implementation.UserAuth.UserAuthServiceImpl;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class User {
    private final UserAuthServiceImpl service;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterReq registerReq, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        return service.registerUser(registerReq, httpRequest);
    }

    @RequestMapping(value = "/auth/confirm", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam String token, @RequestParam String email, HttpServletResponse response) throws ExecutionException, IOException {
        return service.confirmUserAccount(token, email, response);
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

    @PostMapping("/oauth/google/register")
    public ResponseEntity<?> googleRegister(@RequestBody TokenRefreshReq tokenRefreshReq, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        return service.googleRegister(tokenRefreshReq.getRefreshToken(), httpRequest);
    }

    @PostMapping("/oauth/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody TokenRefreshReq tokenRefreshReq) {
        return service.googleLogin(tokenRefreshReq.getRefreshToken());
    }

    @GetMapping("/auth/check")
    public Boolean checkUserExistence(@RequestParam("email")String email) {
        return userRepo.existsByEmailIgnoreCase(email);
    }

    @PutMapping("/profile/choices")
    public ResponseEntity<?> addChoices(@RequestBody ChoicesReq choicesReq, HttpServletRequest httpRequest) {
        String requestTokenHeader =httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.addChoices(email, choicesReq);
    }

    @PutMapping("/profile/change/name")
    public ResponseEntity<?> changeName(@RequestBody NameChangeReq changeReq, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.changeName(email, changeReq.firstName(), changeReq.lastName());
    }

    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok().body("This is a test API");
    }
}
