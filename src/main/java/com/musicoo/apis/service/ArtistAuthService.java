package com.musicoo.apis.service;

import com.musicoo.apis.payload.request.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ArtistAuthService {
    ResponseEntity<?> registerArtist(String registerReq, HttpServletRequest httpRequest, MultipartFile artistImage) throws Exception;
    ResponseEntity<?> sendArtistVerificationLink(ArtistRegisterReq registerReq, String baseURL) throws MessagingException, ExecutionException;
    ResponseEntity<?> confirmArtistAccount(String confirmationToken, String email, HttpServletResponse response) throws ExecutionException, IOException;
    ResponseEntity<?> loginArtist(LoginReq loginReq);
    ResponseEntity<?> forgotArtistPassword(String email) throws ExecutionException, MessagingException;
//    ResponseEntity<?> confirmArtistOTP(ConfirmOTPReq confirmOTPReq) throws ExecutionException;
    ResponseEntity<?> changeArtistPassword(ConfirmOTPReq confirmOTPReq) throws ExecutionException;
    ResponseEntity<?> resetArtistPassword(ResetPassReq resetPassReq);
    ResponseEntity<?> generateAccessToken(String refreshToken);
    ResponseEntity<?> googleRegister(String googleAuthToken, HttpServletRequest httpRequest) throws Exception;
    ResponseEntity<?> googleLogin(String refreshToken);
}
