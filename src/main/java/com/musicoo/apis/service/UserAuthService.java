package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.ConfirmOTPReq;
import com.musicoo.apis.payload.request.EmailReq;
import com.musicoo.apis.payload.request.LoginReq;
import com.musicoo.apis.payload.request.ResetPassReq;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface UserAuthService {
    ResponseEntity<?> registerUser(MusicooUser musicooUser, HttpServletRequest httpRequest) throws MessagingException, ExecutionException;
    ResponseEntity<?> sendUserVerificationLink(MusicooUser musicooUser, String baseURL) throws MessagingException, ExecutionException;
    ResponseEntity<?> confirmUserAccount(String confirmationToken, String email) throws ExecutionException;
    ResponseEntity<?> loginUser(LoginReq loginReq);
    ResponseEntity<?> forgotUserPassword(String email) throws ExecutionException, MessagingException;
    ResponseEntity<?> confirmUserOTP(ConfirmOTPReq confirmOTPReq) throws ExecutionException;
    ResponseEntity<?> changeUserPassword(ConfirmOTPReq confirmOTPReq) throws ExecutionException;
    ResponseEntity<?> resetUserPassword(ResetPassReq resetPassReq);
    ResponseEntity<?> generateAccessToken(String refreshToken);
}
