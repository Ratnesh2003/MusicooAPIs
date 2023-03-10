package com.musicoo.apis.controller.Auth;

import com.musicoo.apis.payload.request.*;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.service.ArtistAuthService;
import com.musicoo.apis.service.Implementation.ArtistAuth.ArtistAuthServiceImpl;
import com.musicoo.apis.service.Implementation.UserAuth.UserAuthServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Artist {
    private final UserAuthServiceImpl service;
    private final ArtistAuthServiceImpl artistService;
    private final ArtistRepo artistRepo;

    @PostMapping("/auth/signup/artist")
    public ResponseEntity<?> registerArtist(@RequestPart String artistRegisterReq, HttpServletRequest httpRequest, @RequestPart("artistImage")MultipartFile artistImage) throws Exception {
//        try {
            return artistService.registerArtist(artistRegisterReq, httpRequest, artistImage);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email is already being used");
//        }
    }

    @RequestMapping(value = "/auth/confirm/artist", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmArtist(@RequestParam String token, @RequestParam String email, HttpServletResponse response) throws ExecutionException, IOException {
        return artistService.confirmArtistAccount(token, email, response);
    }

    @PostMapping("/auth/login/artist")
    public ResponseEntity<?> loginArtist(@RequestBody LoginReq loginReq) {
        return artistService.loginArtist(loginReq);
    }

    @PostMapping("/auth/refresh-token/artist")
    public ResponseEntity<?> refreshTokenArtist(@RequestBody TokenRefreshReq tokenRefreshReq) {
        return artistService.generateAccessToken(tokenRefreshReq.getRefreshToken());
    }

    @PostMapping("/auth/forgot-password/artist")
    public ResponseEntity<?> forgotPasswordArtist(@Valid @RequestBody EmailReq emailReq) throws MessagingException, ExecutionException {
        return artistService.forgotArtistPassword(emailReq.getEmail());
    }

    @PostMapping("/auth/confirm-otp/artist")
    public ResponseEntity<?> confirmOtpArtist(@Valid @RequestBody ConfirmOTPReq confirmOTPReq) throws ExecutionException {
        return service.confirmUserOTP(confirmOTPReq);
    }

    @PostMapping("/auth/change-password/artist")
    public ResponseEntity<?> changePasswordArtist(@RequestBody ConfirmOTPReq confirmOTPReq) throws ExecutionException {
        return artistService.changeArtistPassword(confirmOTPReq);
    }

    @PostMapping("/oauth/google/register/artist")
    public ResponseEntity<?> googleRegisterArtist(@RequestBody TokenRefreshReq tokenRefreshReq, HttpServletRequest httpRequest) throws Exception {
        return artistService.googleRegister(tokenRefreshReq.getRefreshToken(), httpRequest);
    }

    @PostMapping("/oauth/google/login/artist")
    public ResponseEntity<?> googleLoginArtist(@RequestBody TokenRefreshReq tokenRefreshReq) {
        return artistService.googleLogin(tokenRefreshReq.getRefreshToken());
    }

    @GetMapping("/auth/check/artist")
    public Boolean checkArtistExistence(@RequestParam("email")String email) {
        return artistRepo.existsByEmailIgnoreCase(email);
    }

}
