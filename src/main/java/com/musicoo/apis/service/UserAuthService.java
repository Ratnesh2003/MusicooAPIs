package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface UserAuthService {
    ResponseEntity<?> registerUser(RegisterReq registerReq, HttpServletRequest httpRequest) throws MessagingException, ExecutionException;
    ResponseEntity<?> sendUserVerificationLink(RegisterReq registerReq, String baseURL) throws MessagingException, ExecutionException;
    ResponseEntity<?> confirmUserAccount(String confirmationToken, String email, HttpServletResponse response) throws ExecutionException, IOException;
    ResponseEntity<?> loginUser(LoginReq loginReq);
    ResponseEntity<?> forgotUserPassword(String email) throws ExecutionException, MessagingException;
    ResponseEntity<?> confirmUserOTP(ConfirmOTPReq confirmOTPReq) throws ExecutionException;
    ResponseEntity<?> changeUserPassword(ConfirmOTPReq confirmOTPReq) throws ExecutionException;
    ResponseEntity<?> resetUserPassword(ResetPassReq resetPassReq);
    ResponseEntity<?> generateAccessToken(String refreshToken);
    ResponseEntity<?> googleRegister(String googleAuthToken, HttpServletRequest httpRequest) throws MessagingException, ExecutionException;
    ResponseEntity<?> googleLogin(String refreshToken);
    ResponseEntity<?> addChoices(String email, ChoicesReq choicesReq);
}
