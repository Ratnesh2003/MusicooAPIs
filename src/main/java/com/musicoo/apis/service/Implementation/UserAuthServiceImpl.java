package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.ConfirmOTPReq;
import com.musicoo.apis.payload.request.EmailReq;
import com.musicoo.apis.payload.request.LoginReq;
import com.musicoo.apis.payload.request.ResetPassReq;
import com.musicoo.apis.payload.response.TokenRefreshResponse;
import com.musicoo.apis.payload.response.UserInfoResponse;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.EmailService;
import com.musicoo.apis.service.UserAuthService;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.springframework.data.util.CastUtils.cast;


@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenOrOTPServiceImpl tokenOrOTPService;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
//    private final AuthenticationManager authenticationManager;



    @Override
    public ResponseEntity<?> registerUser(MusicooUser musicooUser, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        String baseURL =  ServletUriComponentsBuilder.fromRequestUri(httpRequest).replacePath(null).build().toUriString();
        if (userRepo.existsByEmail(musicooUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        musicooUser.setPassword(passwordEncoder.encode(musicooUser.getPassword()));
        return sendUserVerificationLink(musicooUser, baseURL);
    }

    @Override
    public ResponseEntity<?> sendUserVerificationLink(MusicooUser musicooUser, String baseURL) throws MessagingException, ExecutionException {
        tokenOrOTPService.generateTokenOrOTP(1, musicooUser.getEmail());
        tokenOrOTPService.cacheUserData(musicooUser);

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
            userRepo.save(cast(tokenOrOTPService.getTokenOrOTP(3, email)));
            tokenOrOTPService.clearTokenOrOTP(1, email);
            tokenOrOTPService.clearTokenOrOTP(3, email);
            return ResponseEntity.status(HttpStatus.OK).body("Account verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("The link is invalid");
        }

    }

    @Override
    public ResponseEntity<?> loginUser(LoginReq loginReq) {
        MusicooUser musicooUser = userRepo.findByEmail(loginReq.getEmail());
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(loginReq.getEmail());
        try {
            if(passwordEncoder.matches(loginReq.getPassword(), musicooUser.getPassword())) {
                String jwtCookie = jwtUtil.generateToken(userDetails);
                String refreshJwtCookie = jwtUtil.generateTokenFromEmail(userDetails.getEmail());
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie)
                        .body(new UserInfoResponse(
                                userDetails.getId(),
                                userDetails.getEmail(),
                                userDetails.getFirstName(),
                                userDetails.getLastName(),
                                userDetails.getRole(),
                                jwtCookie,
                                refreshJwtCookie));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Email or Password"); //password incorrect
            }
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password"); //email incorrect
        }

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

    @Override
    public ResponseEntity<?> generateAccessToken(String refreshToken) {
        String email = jwtUtil.getEmailFromToken(refreshToken);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(email);
        if (jwtUtil.validateToken(refreshToken, userDetails)) {
            return ResponseEntity.status(HttpStatus.OK).body(new TokenRefreshResponse(jwtUtil.generateToken(userDetails), refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    }
}
