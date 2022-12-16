package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.User;
import com.musicoo.apis.model.UserEmailOTPConfirmation;
import com.musicoo.apis.payload.request.ConfirmOTPReq;
import com.musicoo.apis.payload.request.EmailReq;
import com.musicoo.apis.payload.request.LoginReq;
import com.musicoo.apis.payload.request.ResetPassReq;
import com.musicoo.apis.repository.ArtistEmailOTPConfirmationRepo;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.UserEmailOTPConfirmationRepo;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.EmailService;
import com.musicoo.apis.service.UserAuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {
    private UserRepo userRepo;
    private ArtistRepo artistRepo;
    private PasswordEncoder passwordEncoder;
    private UserEmailOTPConfirmationRepo userEmailOTPConfirmationRepo;
    private ArtistEmailOTPConfirmationRepo artistEmailOTPConfirmationRepo;
    private EmailService emailService;



    @Override
    public ResponseEntity<?> registerUser(User user, HttpServletRequest httpRequest) throws MessagingException {
        String baseURL =  ServletUriComponentsBuilder.fromRequestUri(httpRequest).replacePath(null).build().toUriString();
        if (userRepo.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);


        return sendUserVerificationLink(user, baseURL);
    }

    @Override
    public ResponseEntity<?> sendUserVerificationLink(User user, String baseURL) throws MessagingException {
        String confirmationToken = UUID.randomUUID().toString();
        UserEmailOTPConfirmation userEmailOTPConfirmation = new UserEmailOTPConfirmation(user, confirmationToken, new Date(), 0, null);
        userEmailOTPConfirmationRepo.save(userEmailOTPConfirmation);

        emailService.sendMessageWithAttachment(
                "drreamboy9@gmail.com",
                user.getEmail(),
                "Email Verification Musicoo",
                "Hello " + user.getFirstName() + " " + user.getLastName() + ",<br><br> You registered an account on Musicoo, " +
                        "before being able to use your account you need to verify that this is your email address by clicking " +
                        "<a href=\""+ baseURL + "/api/auth/confirm-account?token=" + userEmailOTPConfirmation.getConfirmationToken() +
                        "\">here</a>" + ".<br><br>Kind Regards, Felix"
        );
        return ResponseEntity.status(HttpStatus.OK).body("Please check your email for verification");
    }

    @Override
    public ResponseEntity<?> confirmUserAccount(String confirmationToken) {
        UserEmailOTPConfirmation tokenModel =userEmailOTPConfirmationRepo.findByConfirmationToken(confirmationToken);
        if (tokenModel == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("The link is invalid");
        }
        if(new Date().getTime() > tokenModel.getTokenCreationDate().getTime() + 5*60*1000) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Verification link expired");
        } else {
            User user = userRepo.findUserById(tokenModel.getUser().getId());
            user.setIsEnabled(true);
            userRepo.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("Account verified");

        }
    }

    @Override
    public ResponseEntity<?> loginUser(LoginReq loginReq) {
        return null;
    }

    @Override
    public ResponseEntity<?> forgotUserPassword(EmailReq emailReq) {
        return null;
    }

    @Override
    public ResponseEntity<?> confirmUserOTP(ConfirmOTPReq confirmOTPReq) {
        return null;
    }

    @Override
    public ResponseEntity<?> changeUserPassword(ConfirmOTPReq confirmOTPReq) {
        return null;
    }

    @Override
    public ResponseEntity<?> resetUserPassword(ResetPassReq resetPassReq) {
        return null;
    }
}
