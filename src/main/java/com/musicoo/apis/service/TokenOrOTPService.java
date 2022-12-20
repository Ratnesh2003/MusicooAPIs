package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.request.RegisterReq;

import java.util.concurrent.ExecutionException;

public interface TokenOrOTPService {
    public void cacheUserData(RegisterReq registerReq);
    public Object generateTokenOrOTP(int choice, String key);
    public <T> T getTokenOrOTP(int choice, String key) throws ExecutionException;
    public void clearTokenOrOTP(int choice, String key);
}

