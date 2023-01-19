package com.musicoo.apis.service.jwt;

import com.musicoo.apis.service.Implementation.ArtistAuth.ArtistDetailsImpl;
import com.musicoo.apis.service.Implementation.UserAuth.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@NoArgsConstructor
public class JwtUtil {

    public static final long ACCESS_JWT_TOKEN_VALIDITY =  24 * 60 * 60;
    public static final long REFRESH_JWT_TOKEN_VALIDITY = 30 * 24 * 60 * 60;

    private String secret = "MusicooSpringSecurityIamWritingThisJustToExtendTheSizeOfTheSecurityKeyIThinkItShouldWorkNow";

    //retrieve username from jwt token
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getEmail());
    }

    public String generateArtistToken(ArtistDetailsImpl artistDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, artistDetails.getEmail());
    }

    public String generateTokenFromEmail(String email) {
        return Jwts.builder().setSubject(email).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetailsImpl userDetails) {
        final String email = getEmailFromToken(token);
        System.out.println("Checking if the token is valid or not:  " + (email.equals(userDetails.getEmail()) && !isTokenExpired(token)));
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public Boolean validateArtistToken(String token, ArtistDetailsImpl artistDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(artistDetails.getEmail()) && !isTokenExpired(token));
    }


}

