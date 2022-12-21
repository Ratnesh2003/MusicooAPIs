package com.musicoo.apis.service.Implementation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.ArtistRegisterReq;
import com.musicoo.apis.payload.request.RegisterReq;
import com.musicoo.apis.service.TokenOrOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class TokenOrOTPServiceImpl implements TokenOrOTPService {

    private static final Integer EXPIRE_MINUTES = 5;
    private LoadingCache<String, String> tokenCache;
    private LoadingCache<String, Integer> otpCache;
    private LoadingCache<String, RegisterReq> userCache;
    private LoadingCache<String, ArtistRegisterReq> artistCache;

    public TokenOrOTPServiceImpl() {
        super();
        tokenCache = CacheBuilder.newBuilder().
                    expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
                    .build(new CacheLoader<String, String>() {
                        @Override
                        public String load(String s) throws Exception {
                            return null;
                        }
                    });
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return null;
                    }
                });
        userCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<String, RegisterReq>() {
                    @Override
                    public RegisterReq load(String s) throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void cacheUserData(RegisterReq registerReq) {
        userCache.put(registerReq.getEmail(), registerReq);
    }

    @Override
    public void cacheArtistData(ArtistRegisterReq registerReq) {
        artistCache.put(registerReq.getEmail(), registerReq);
    }

    @Override
    public Object generateTokenOrOTP(int choice, String key) {
        if (choice == 1) {
            String confirmationToken = UUID.randomUUID().toString();
            tokenCache.put(key, confirmationToken);
            return null;
        } else {
            Random random = new Random();
            int otp = random.nextInt(899999) + 100000;
            otpCache.put(key, otp);
            return otp;
        }
    }

    @Override
    public Object getTokenOrOTP(int choice, String key) throws CacheLoader.InvalidCacheLoadException {
        try{
            if (choice == 1) {
                return tokenCache.get(key);
            } else if (choice == 2) {
                return otpCache.get(key);
            } else if (choice == 3){
                return userCache.get(key);
            } else {
                return artistCache.get(key);
            }
        } catch (ExecutionException | CacheLoader.InvalidCacheLoadException e) {
            return null;
        }
    }

    @Override
    public void clearTokenOrOTP(int choice, String key) {
        if (choice == 1) {
            tokenCache.invalidate(key);
        } else if (choice == 2){
            otpCache.invalidate(key);
        } else if (choice == 3){
            userCache.invalidate(key);
        } else if (choice == 4){
            artistCache.invalidate(key);
        }
    }
}
