package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooUser;

import java.util.concurrent.ExecutionException;

public interface TokenOrOTPService {
    public void cacheUserData(MusicooUser musicooUser);
    public Object generateTokenOrOTP(int choice, String key);
    public <T> T getTokenOrOTP(int choice, String key) throws ExecutionException;
    public void clearTokenOrOTP(int choice, String key);
}

