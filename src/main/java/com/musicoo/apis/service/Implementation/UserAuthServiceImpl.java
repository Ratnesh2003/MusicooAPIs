package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.MusicooUser;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserEmailOTPConfirmationRepo userEmailOTPConfirmationRepo;
    private final ArtistEmailOTPConfirmationRepo artistEmailOTPConfirmationRepo;
    private final EmailService emailService;
    private final TokenOrOTPServiceImpl tokenOrOTPService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public ResponseEntity<?> registerUser(MusicooUser musicooUser, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        String baseURL =  ServletUriComponentsBuilder.fromRequestUri(httpRequest).replacePath(null).build().toUriString();
        if (userRepo.existsByEmail(musicooUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        musicooUser.setPassword(passwordEncoder.encode(musicooUser.getPassword()));
        userRepo.save(musicooUser);


        return sendUserVerificationLink(musicooUser, baseURL);
    }

    @Override
    public ResponseEntity<?> sendUserVerificationLink(MusicooUser musicooUser, String baseURL) throws MessagingException, ExecutionException {
//        String confirmationToken = UUID.randomUUID().toString();
//        UserEmailOTPConfirmation userEmailOTPConfirmation = new UserEmailOTPConfirmation(musicooUser, confirmationToken, new Date(), 0, null);
//        userEmailOTPConfirmationRepo.save(userEmailOTPConfirmation);
//        tokenOrOTPService.cacheTokenOrOTP(1);
        tokenOrOTPService.generateTokenOrOTP(1, musicooUser.getEmail());



        emailService.sendMessageWithAttachment(
                "drreamboy9@gmail.com",
                musicooUser.getEmail(),
                "Email Verification Musicoo",
                "Hello " + musicooUser.getFirstName() + " " + musicooUser.getLastName() + ",<br><br> You registered an account on Musicoo, " +
                        "before being able to use your account you need to verify that this is your email address by clicking " +
                        "<a href=\""+ baseURL + "/api/auth/confirm?token=" + tokenOrOTPService.getTokenOrOTP(1, musicooUser.getEmail()).toString() +
                        "&email=" + musicooUser.getEmail() + "\">here</a>" + ".<br><br>Kind Regards, Felix"
        );
        return ResponseEntity.status(HttpStatus.OK).body("Please check your email for verification");
    }

    @Override
    public ResponseEntity<?> confirmUserAccount(String confirmationToken, String email) throws ExecutionException {
        if (Objects.equals(tokenOrOTPService.getTokenOrOTP(1, email).toString(), confirmationToken)) {
            MusicooUser musicooUser = userRepo.findByEmail(email);
            musicooUser.setIsEnabled(true);
            userRepo.save(musicooUser);
            tokenOrOTPService.clearTokenOrOTP(1, email);
            return ResponseEntity.status(HttpStatus.OK).body("Account verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("The link is invalid");
        }

//        UserEmailOTPConfirmation tokenModel =userEmailOTPConfirmationRepo.findByConfirmationToken(confirmationToken);
//        if (tokenModel == null) {
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("The link is invalid");
//        }
//        if(new Date().getTime() > tokenModel.getTokenCreationDate().getTime() + 5*60*1000) {
//            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Verification link expired");
//        } else {
//            MusicooUser musicooUser = userRepo.findUserById(tokenModel.getMusicooUser().getId());
//            musicooUser.setIsEnabled(true);
//            userRepo.save(musicooUser);
//            return ResponseEntity.status(HttpStatus.OK).body("Account verified");
//
//        }
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
