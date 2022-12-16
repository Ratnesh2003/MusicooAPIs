package com.musicoo.apis.service;

import java.util.concurrent.ExecutionException;

public interface TokenOrOTPService {
//    public void cacheTokenOrOTP(int choice);
    public <T> T generateTokenOrOTP(int choice, String key);
    public <T> T getTokenOrOTP(int choice, String key) throws ExecutionException;
    public void clearTokenOrOTP(int choice, String key);
}

