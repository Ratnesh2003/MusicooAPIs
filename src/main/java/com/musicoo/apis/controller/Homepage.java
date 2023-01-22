package com.musicoo.apis.controller;

import com.musicoo.apis.payload.request.OnlyIdReq;
import com.musicoo.apis.service.Implementation.HomepageServiceImpl;
import com.musicoo.apis.service.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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

    @GetMapping("/home/charts/top/{lang}")
    public ResponseEntity<?> getTopCharts(@PathVariable String lang, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.getTopCharts(lang, email);
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

    @GetMapping("/artists/top")
    public ResponseEntity<?> getTopArtists() {
        return service.topArtists();
    }

    @GetMapping("/artist/view/{id}")
    public ResponseEntity<?> getArtist(@PathVariable Long id) {
        return service.viewArtist(id);
    }

    @GetMapping("/song/{id}")
    public ResponseEntity<?> getSong(@PathVariable Long id, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.listenSong(id, email);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String searchText) {
        return service.search(searchText);
    }

    @GetMapping("/songs/random")
    public ResponseEntity<?> randomSongs(HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.searchRandomSongs(email);
    }

    @GetMapping("/check/liked")
    public ResponseEntity<?> checkLiked(@RequestParam("songId") long songId, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.checkLiked(songId, email);
    }


}
