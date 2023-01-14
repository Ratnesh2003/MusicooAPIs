package com.musicoo.apis.service;

import com.musicoo.apis.payload.request.LikedReq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface HomepageService {
    public ResponseEntity<?> quickPicks(String home);
    public ResponseEntity<?> addToLiked(LikedReq likedReq, String email);
    public ResponseEntity<?> getAllPlaylists(String email);
    public ResponseEntity<?> getSongsOfPlaylist(long pId, String email);
    public ResponseEntity<?> getLikedSongs(String email);
    public ResponseEntity<?> getTopCharts();
}
