package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.ConfirmOTPReq;
import com.musicoo.apis.payload.request.EmailReq;
import com.musicoo.apis.payload.request.LoginReq;
import com.musicoo.apis.payload.request.ResetPassReq;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserAuthService {
    ResponseEntity<?> registerUser(MusicooUser musicooUser, HttpServletRequest httpRequest) throws MessagingException;
    ResponseEntity<?> sendUserVerificationLink(MusicooUser musicooUser, String baseURL) throws MessagingException;
    ResponseEntity<?> confirmUserAccount(String confirmationToken);
    ResponseEntity<?> loginUser(LoginReq loginReq);
    ResponseEntity<?> forgotUserPassword(EmailReq emailReq);
    ResponseEntity<?> confirmUserOTP(ConfirmOTPReq confirmOTPReq);
    ResponseEntity<?> changeUserPassword(ConfirmOTPReq confirmOTPReq);
    ResponseEntity<?> resetUserPassword(ResetPassReq resetPassReq);
}
