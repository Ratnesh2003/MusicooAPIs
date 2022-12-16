package com.musicoo.apis.service;

import com.musicoo.apis.model.User;
import com.musicoo.apis.payload.request.ConfirmOTPReq;
import com.musicoo.apis.payload.request.EmailReq;
import com.musicoo.apis.payload.request.LoginReq;
import com.musicoo.apis.payload.request.ResetPassReq;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserAuthService {
    ResponseEntity<?> registerUser(User user, HttpServletRequest httpRequest) throws MessagingException;
    ResponseEntity<?> sendUserVerificationLink(User user, String baseURL) throws MessagingException;
    ResponseEntity<?> confirmUserAccount(String confirmationToken);
    ResponseEntity<?> loginUser(LoginReq loginReq);
    ResponseEntity<?> forgotUserPassword(EmailReq emailReq);
    ResponseEntity<?> confirmUserOTP(ConfirmOTPReq confirmOTPReq);
    ResponseEntity<?> changeUserPassword(ConfirmOTPReq confirmOTPReq);
    ResponseEntity<?> resetUserPassword(ResetPassReq resetPassReq);
}
