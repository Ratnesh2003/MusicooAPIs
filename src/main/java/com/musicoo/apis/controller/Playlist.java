package com.musicoo.apis.controller;


import com.musicoo.apis.payload.request.OnlyIdReq;
import com.musicoo.apis.payload.request.PlaylistAddReq;
import com.musicoo.apis.payload.request.PlaylistNameReq;
import com.musicoo.apis.service.Implementation.PlaylistService;
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
public class Playlist {

    private final PlaylistService service;
    private final JwtUtil jwtUtil;

    @PostMapping("/song/add-to-liked")
    public ResponseEntity<?> addToLiked(@RequestBody OnlyIdReq onlyIdReq, HttpServletRequest httpRequest) {
        String requestTokenHeader =httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.likeUnlike(onlyIdReq, email);
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

    @PostMapping("/playlist/create")
    public ResponseEntity<?> createPlaylist(HttpServletRequest httpRequest,@RequestBody PlaylistNameReq playlistNameReq) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.createPlaylist(email, playlistNameReq.name());
    }

    @PostMapping("/playlist/add")
    public ResponseEntity<?> addToPlaylist(@RequestBody PlaylistAddReq playlistAddReq, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.addToPlaylist(playlistAddReq.songId(), playlistAddReq.playlistId(), email);
    }

    @DeleteMapping("/playlist/delete")
    @Transactional
    public ResponseEntity<?> deletePlaylist(@RequestParam("playlistId") long playlistId, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.deletePlaylist(playlistId, email);
    }

    @DeleteMapping("/playlist/song/remove")
    @Transactional
    public ResponseEntity<?> removeSongFromPlaylist(@RequestParam("playlistId") long playlistId, @RequestParam("songId")     long songId, HttpServletRequest httpRequest) {
        String requestTokenHeader = httpRequest.getHeader("Authorization");
        String email = jwtUtil.getEmailFromToken(requestTokenHeader.substring(7));
        return service.removeFromPlaylist(playlistId, songId, email);
    }




}
