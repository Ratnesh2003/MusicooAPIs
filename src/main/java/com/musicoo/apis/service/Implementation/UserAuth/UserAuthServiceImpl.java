package com.musicoo.apis.service.Implementation.UserAuth;

import com.google.common.cache.CacheLoader;
import com.musicoo.apis.helper.TokenDecoder;
import com.musicoo.apis.model.Genre;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.enums.Provider;
import com.musicoo.apis.payload.request.*;
import com.musicoo.apis.payload.response.TokenRefreshResponse;
import com.musicoo.apis.payload.response.UserDetailsRes;
import com.musicoo.apis.payload.response.UserInfoResponse;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.GenreRepo;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.EmailService;
import com.musicoo.apis.service.Implementation.TokenOrOTPServiceImpl;
import com.musicoo.apis.service.UserAuthService;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final TokenDecoder tokenDecoder;
    private final ArtistRepo artistRepo;
    private final GenreRepo genreRepo;



    @Override
    public ResponseEntity<?> registerUser(RegisterReq registerReq, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        String baseURL =  ServletUriComponentsBuilder.fromRequestUri(httpRequest).replacePath(null).build().toUriString();
        if (userRepo.existsByEmailIgnoreCase(registerReq.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        registerReq.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        return sendUserVerificationLink(registerReq, baseURL);
    }

    @Override
    public ResponseEntity<?> sendUserVerificationLink(RegisterReq registerReq, String baseURL) throws MessagingException, ExecutionException {
        tokenOrOTPService.generateTokenOrOTP(1, registerReq.getEmail());
        tokenOrOTPService.cacheUserData(registerReq);

        emailService.sendMessageWithAttachment(
                "drreamboy9@gmail.com",
                registerReq.getEmail(),
                "Email Verification Musicoo",
                "Hello " + registerReq.getFirstName() + " " + registerReq.getLastName() + ",<br><br> You registered an account on Musicoo, " +
                        "before being able to use your account you need to verify that this is your email address by clicking " +
                        "<a href=\""+ baseURL + "/api/auth/confirm?token=" + tokenOrOTPService.getTokenOrOTP(1, registerReq.getEmail()).toString() +
                        "&email=" + registerReq.getEmail() + "\">here</a>" + ".<br><br>Kind Regards, Musicoo"
        );
        return ResponseEntity.status(HttpStatus.OK).body("Please check your email for verification");
    }

    @Override
    public ResponseEntity<?> confirmUserAccount(String confirmationToken, String email, HttpServletResponse response) throws ExecutionException, IOException {
        if (Objects.equals(tokenOrOTPService.getTokenOrOTP(1, email).toString(), confirmationToken)) {
            RegisterReq registerReq = cast(tokenOrOTPService.getTokenOrOTP(3, email));
            MusicooUser musicooUser = new MusicooUser(
                    registerReq.getFirstName(),
                    registerReq.getLastName(),
                    registerReq.getEmail(),
                    registerReq.getPassword(),
                    registerReq.getProvider()
            );
            userRepo.save(musicooUser);
            tokenOrOTPService.clearTokenOrOTP(1, email);
            tokenOrOTPService.clearTokenOrOTP(3, email);
            response.sendRedirect("http://musicoo.app.link/verified");
            return ResponseEntity.status(HttpStatus.OK).body("Account verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("The link is invalid");
        }

    }

    @Override
    public ResponseEntity<?> loginUser(LoginReq loginReq) {
        MusicooUser musicooUser = userRepo.findByEmailIgnoreCase(loginReq.getEmail());
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(loginReq.getEmail());
        try {
            if(passwordEncoder.matches(loginReq.getPassword(), musicooUser.getPassword())) {
                String jwtCookie = jwtUtil.generateToken(userDetails);
                String refreshJwtCookie = jwtUtil.generateTokenFromEmail(userDetails.getEmail());
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie)
                        .body(new UserInfoResponse(
                                userDetails.getId(),
                                userDetails.getFirstName(),
                                userDetails.getLastName(),
                                userDetails.getEmail(),
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
    public ResponseEntity<?> forgotUserPassword(String email) throws ExecutionException, MessagingException {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        if (user != null) {
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

    @Override
    public ResponseEntity<?> confirmUserOTP(ConfirmOTPReq confirmOTPReq) throws ExecutionException, CacheLoader.InvalidCacheLoadException {
        try {
            if (Objects.equals(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()), confirmOTPReq.getOtp())) {
                return ResponseEntity.status(HttpStatus.OK).body("OTP Verified");
            } else if(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Expired");
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Incorrect OTP");
            }
        } catch (CacheLoader.InvalidCacheLoadException e) {
            return null;
        }
    }



    @Override
    public ResponseEntity<?> changeUserPassword(ConfirmOTPReq confirmOTPReq) throws ExecutionException, CacheLoader.InvalidCacheLoadException {

        try {
            if (Objects.equals(tokenOrOTPService.getTokenOrOTP(2, confirmOTPReq.getEmail()), confirmOTPReq.getOtp())) {
                MusicooUser user = userRepo.findByEmailIgnoreCase(confirmOTPReq.getEmail());
                user.setPassword(passwordEncoder.encode(confirmOTPReq.getPassword()));
                userRepo.save(user);
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

    @Override
    public ResponseEntity<?> googleRegister(String googleAuthToken, HttpServletRequest httpRequest) throws MessagingException, ExecutionException {
        RegisterReq registerReq = tokenDecoder.getRegisterRequestFromToken(googleAuthToken);
        MusicooUser user = new MusicooUser(
                registerReq.getFirstName(),
                registerReq.getLastName(),
                registerReq.getEmail().trim(),
                "xxx",
                Provider.GOOGLE
        );
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }

    @Override
    public ResponseEntity<?> googleLogin(String googleAuthToken) throws UsernameNotFoundException {
        RegisterReq registerReq = tokenDecoder.getRegisterRequestFromToken(googleAuthToken);
        if (!userRepo.existsByEmailIgnoreCase(registerReq.getEmail())) {
            MusicooUser musicooUser = new MusicooUser(
                    registerReq.getFirstName(),
                    registerReq.getLastName(),
                    registerReq.getEmail(),
                    registerReq.getPassword(),
                    registerReq.getProvider()
            );
            userRepo.save(musicooUser);
        }
        try {
            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(registerReq.getEmail());
            String jwtCookie = jwtUtil.generateToken(userDetails);
            String refreshJwtCookie = jwtUtil.generateTokenFromEmail(userDetails.getEmail());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie)
                    .body(new UserInfoResponse(
                            userDetails.getId(),
                            userDetails.getFirstName(),
                            userDetails.getLastName(),
                            userDetails.getEmail().trim(),
                            userDetails.getRole(),
                            jwtCookie,
                            refreshJwtCookie));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Token");
        }

    }

    @Override
    public ResponseEntity<?> addChoices(String email, ChoicesReq choicesReq) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        List<MusicooArtist> artists = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        for (int i=0; i< choicesReq.getArtistCount(); i++) {
            int id = choicesReq.getArtists().get(i);
            MusicooArtist artist = artistRepo.findById(id);
            artists.add(artist);
        }
        for (int i=0; i<choicesReq.getGenreCount(); i++) {
            int id = choicesReq.getGenres().get(i);
            Genre genre = genreRepo.findById(id);
            genres.add(genre);
        }
        user.setLikedArtists(artists);
        user.setLikedGenres(genres);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Details updated successfully");
    }

    public ResponseEntity<?> changeName(String email, String firstName, String lastName) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepo.save(user);
        return ResponseEntity.ok().body("Name changed successfully");
    }

    public ResponseEntity<?> viewUserProfile(String email) {
        List<?> user = userRepo.findLimitedByEmail(email);
        return ResponseEntity.ok().body(user.get(0));
    }
}
