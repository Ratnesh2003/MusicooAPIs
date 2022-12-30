package com.musicoo.apis.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface HomepageService {
    public ResponseEntity<?> quickPicks(String home);
}
