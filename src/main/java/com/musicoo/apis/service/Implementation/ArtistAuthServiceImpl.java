package com.musicoo.apis.service.Implementation;

import com.google.common.cache.CacheLoader;
import com.musicoo.apis.helper.TokenDecoder;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.Provider;
import com.musicoo.apis.payload.request.*;
import com.musicoo.apis.payload.response.TokenRefreshResponse;
import com.musicoo.apis.payload.response.UserInfoResponse;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.service.ArtistAuthService;
import com.musicoo.apis.service.EmailService;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.springframework.data.util.CastUtils.cast;

@RequiredArgsConstructor
@Service
public class ArtistAuthServiceImpl implements ArtistAuthService {
    private final ArtistRepo artistRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenOrOTPServiceImpl tokenOrOTPService;
    private final EmailService emailService;
    private final ArtistDetailsServiceImpl artistDetailsService;
    private final JwtUtil jwtUtil;
    private final TokenDecoder tokenDecoder;

    @Override
    public ResponseEntity<?> registerArtist(ArtistRegisterReq registerReq, HttpServletRequest httpRequest) throws Exception {
        String baseURL =  ServletUriComponentsBuilder.fromRequestUri(httpRequest).replacePath(null).build().toUriString();
        if (artistRepo.existsByEmailIgnoreCase(registerReq.getEmail())) {
            throw new Exception("Email already in use");
        }
        registerReq.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        return sendArtistVerificationLink(registerReq, baseURL);
    }

    @Override
    public ResponseEntity<?> sendArtistVerificationLink(ArtistRegisterReq registerReq, String baseURL) throws MessagingException {
        try {
            tokenOrOTPService.generateTokenOrOTP(1, registerReq.getEmail());
            tokenOrOTPService.cacheArtistData(registerReq);
            emailService.sendMessageWithAttachment(
                    "drreamboy9@gmail.com",
                    registerReq.getEmail(),
                    "Email Verification Musicoo",
                    "Hello " + registerReq.getFirstName() + " " + registerReq.getLastName() + ",<br><br> You registered an account on Musicoo, " +
                            "before being able to use your account you need to verify that this is your email address by clicking " +
                            "<a href=\""+ baseURL + "/api/auth/confirm?token=" + tokenOrOTPService.getTokenOrOTP(1, registerReq.getEmail()).toString() +
                            "&email=" + registerReq.getEmail() + "\">here</a>" + ".<br><br>Kind Regards, Musicoo"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Please check your email for verification");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred while sending email");
        }



    }

    @Override
    public ResponseEntity<?> confirmArtistAccount(String confirmationToken, String email) throws ExecutionException {
        if (Objects.equals(tokenOrOTPService.getTokenOrOTP(4, email).toString(), confirmationToken)) {
            ArtistRegisterReq registerReq = cast(tokenOrOTPService.getTokenOrOTP(4, email));
            MusicooArtist musicooArtist = new MusicooArtist(
                    registerReq.getFirstName(),
                    registerReq.getLastName(),
                    registerReq.getEmail(),
                    registerReq.getPassword(),
                    Provider.LOCAL
            );
            artistRepo.save(musicooArtist);
            tokenOrOTPService.clearTokenOrOTP(1, email);
            tokenOrOTPService.clearTokenOrOTP(4, email);
            return ResponseEntity.status(HttpStatus.OK).body("Account verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("The link is invalid");
        }
    }

    @Override
    public ResponseEntity<?> loginArtist(LoginReq loginReq) {
        MusicooArtist musicooArtist = artistRepo.findByEmailIgnoreCase(loginReq.getEmail());
        ArtistDetailsImpl artistDetails = artistDetailsService.loadUserByUsername(loginReq.getEmail());
        try {
            if(passwordEncoder.matches(loginReq.getPassword(),  musicooArtist.getPassword())) {
                String jwtCookie = jwtUtil.generateArtistToken(artistDetails);
                String refreshJwtCookie = jwtUtil.generateTokenFromEmail(artistDetails.getEmail());
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie)
                        .body(new UserInfoResponse(
                                artistDetails.getId(),
                                artistDetails.getEmail(),
                                artistDetails.getFirstName(),
                                artistDetails.getLastName(),
                                artistDetails.getRole(),
                                jwtCookie,
                                refreshJwtCookie));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password"); //password incorrect
            }
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Email or Password"); //email incorrect
        }
    }

    @Override
    public ResponseEntity<?> forgotArtistPassword(String email) throws ExecutionException, MessagingException {
        MusicooArtist artist = artistRepo.findByEmailIgnoreCase(email);
        if (artist != null) {
            int otp = (int)tokenOrOTPService.generateTokenOrOTP(2, email);
            emailService.sendMessageWithAttachment(
                    "drreamboy9@gmail.com",
                    email, "RESET PASSWORD MUSICOO",
                    "OTP to reset password:  <b>"  + Integer.toString(otp) + "</b>"
            );
            return ResponseEntity.ok().body("OTP sent on the given mail");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artist not found with the given email");
        }
    }
//
//    @Override
//    public ResponseEntity<?> confirmArtistOTP(ConfirmOTPReq confirmOTPReq) throws ExecutionException {
//        try {
//            if (Objects.equals(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()), confirmOTPReq.getOtp())) {
//                return ResponseEntity.status(HttpStatus.OK).body("OTP Verified");
//            } else if(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()) == null){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Expired");
//            } else {
//                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Incorrect OTP");
//            }
//        } catch (CacheLoader.InvalidCacheLoadException e) {
//            return null;
//        }
//    } //call this method from userauthservice

    @Override
    public ResponseEntity<?> changeArtistPassword(ConfirmOTPReq confirmOTPReq) {
        try {
            if (Objects.equals(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()), confirmOTPReq.getOtp())) {
                MusicooArtist artist = artistRepo.findByEmailIgnoreCase(confirmOTPReq.getEmail());
                artist.setPassword(passwordEncoder.encode(confirmOTPReq.getPassword()));
                artistRepo.save(artist);
                tokenOrOTPService.clearTokenOrOTP(2, confirmOTPReq.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
            } else if(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Expired");
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Incorrect OTP");
            }
        } catch (CacheLoader.InvalidCacheLoadException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expired");
        }
    }

    @Override
    public ResponseEntity<?> resetArtistPassword(ResetPassReq resetPassReq) {
        return null;
    }

    @Override
    public ResponseEntity<?> generateAccessToken(String refreshToken) {
        String email = jwtUtil.getEmailFromToken(refreshToken);
        ArtistDetailsImpl artistDetails = artistDetailsService.loadUserByUsername(email);
        if (jwtUtil.validateArtistToken(refreshToken, artistDetails)) {
            return ResponseEntity.status(HttpStatus.OK).body(new TokenRefreshResponse(jwtUtil.generateArtistToken(artistDetails), refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    }

    @Override
    public ResponseEntity<?> googleRegister(String googleAuthToken, HttpServletRequest httpRequest) throws Exception {
        RegisterReq registerReq = tokenDecoder.getRegisterRequestFromToken(googleAuthToken);
        ArtistRegisterReq artistReg = new ArtistRegisterReq(
                registerReq.getFirstName(),
                registerReq.getLastName(),
                registerReq.getEmail(),
                "xxx"
        );
        return registerArtist(artistReg, httpRequest);
    }

    @Override
    public ResponseEntity<?> googleLogin(String refreshToken) {
        try {
            RegisterReq registerReq = tokenDecoder.getRegisterRequestFromToken(refreshToken);
            ArtistDetailsImpl artistDetails = artistDetailsService.loadUserByUsername(registerReq.getEmail());
            String jwtCookie = jwtUtil.generateArtistToken(artistDetails);
            String refreshJwtCookie = jwtUtil.generateTokenFromEmail(artistDetails.getEmail());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie)
                    .body(new UserInfoResponse(
                            artistDetails.getId(),
                            artistDetails.getEmail(),
                            artistDetails.getFirstName(),
                            artistDetails.getLastName(),
                            artistDetails.getRole(),
                            jwtCookie,
                            refreshJwtCookie));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Token");
        }
    }
}