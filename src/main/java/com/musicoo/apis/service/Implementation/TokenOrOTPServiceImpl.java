package com.musicoo.apis.service.Implementation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
    }

//
//    public TokenOrOTPServiceImpl(LoadingCache<String, String> tokenCache) {
//        this.tokenCache = tokenCache;
//    }

//    public void cacheTokenOrOTP(int choice) {
//        if (choice == 1) {
//            tokenCache = CacheBuilder.newBuilder().
//                    expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
//                    .build(new CacheLoader<String, String>() {
//                        @Override
//                        public String load(String s) throws Exception {
//                            return null;
//                        }
//                    });
//        } else if (choice == 2) {
//            otpCache = CacheBuilder.newBuilder().
//                    expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
//                    .build(new CacheLoader<String, Integer>() {
//                        @Override
//                        public Integer load(String key) throws Exception {
//                            return null;
//                        }
//                    });
//        }
//    }

    @Override
    public Object generateTokenOrOTP(int choice, String key) {
        if (choice == 1) {
            String confirmationToken = UUID.randomUUID().toString();
            tokenCache.put(key, confirmationToken);
            return confirmationToken;
        } else {
            Random random = new Random();
            int otp = random.nextInt(899999) + 100000;
            otpCache.put(key, otp);
            return otp;
        }
    }

    @Override
    public Object getTokenOrOTP(int choice, String key) throws ExecutionException {
        if (choice == 1) {
            try {
                return tokenCache.get(key);
            } catch (ExecutionException e) {
                return null;
            }
        } else {
            try {
                return otpCache.get(key);
            } catch (ExecutionException e) {
                return null;
            }
        }
    }

    @Override
    public void clearTokenOrOTP(int choice, String key) {
        if (choice == 1) {
            tokenCache.invalidate(key);
        } else {
            otpCache.invalidate(key);
        }
    }
}
