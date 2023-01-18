package com.musicoo.apis.controller;

import com.musicoo.apis.payload.request.OnlyIdReq;
import com.musicoo.apis.service.Implementation.HomepageServiceImpl;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addToLiked(@RequestBody OnlyIdReq onlyIdReq, HttpServletRequest httpRequest) {
        String requestTokenHeader =httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.addToLiked(onlyIdReq, email);
    }

    @GetMapping("/playlists")
    public ResponseEntity<?> getAllPlaylists(HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.getAllPlaylists(email);
    }

    @GetMapping("/playlist/{id}")
    public ResponseEntity<?> getSongsOfPlaylist(@PathVariable long id, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.getSongsOfPlaylist(id, email);
    }

    @GetMapping("/playlist/liked")
    public ResponseEntity<?> getLikedSongs(HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.getLikedSongs(email);
    }

    @GetMapping("/home/charts/top/{lang}")
    public ResponseEntity<?> getTopCharts(@PathVariable String lang) {
        return service.getTopCharts(lang);
    }

    @GetMapping("/artists")
    public ResponseEntity<?> getAllArtists() {
        return service.allArtists();
    }

    @GetMapping("/genres")
    public ResponseEntity<?> getAllGenres() {
        return service.allGenres();
    }

    @PostMapping("/history/add")
    public ResponseEntity<?> addToHistory(@RequestBody OnlyIdReq onlyIdReq, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.addToHistory(onlyIdReq.songId(), email);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getFullHistory(HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.getFullHistory(email);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentHistory(HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.getRecentlyPlayed(email);
    }


}
