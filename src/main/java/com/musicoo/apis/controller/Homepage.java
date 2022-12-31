package com.musicoo.apis.controller;

import com.musicoo.apis.payload.request.LikedReq;
import com.musicoo.apis.service.Implementation.HomepageServiceImpl;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@RequiredArgsConstructor
@Controller
public class Homepage {

    private final HomepageServiceImpl service;
    private final JwtUtil jwtUtil;

    @GetMapping("/home/quick-picks")
    public ResponseEntity<?> quickPicks(HttpServletRequest httpRequest) {
        String requestTokenHeader =httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.quickPicks(email);
    }

    @PostMapping("/song/add-to-liked")
    public ResponseEntity<?> addToLiked(@RequestBody LikedReq likedReq, HttpServletRequest httpRequest) {
        String requestTokenHeader =httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.addToLiked(likedReq, email);
    }

}
