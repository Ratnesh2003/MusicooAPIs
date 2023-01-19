package com.musicoo.apis.service;

import com.musicoo.apis.payload.request.OnlyIdReq;
import org.springframework.http.ResponseEntity;

public interface HomepageService {
    public ResponseEntity<?> quickPicks(String home);
//    public ResponseEntity<?> addToLiked(OnlyIdReq onlyIdReq, String email);
//    public ResponseEntity<?> addToPlaylist(Long songId, Long playlistId, String email);
//    public ResponseEntity<?> getAllPlaylists(String email);
//    public ResponseEntity<?> getSongsOfPlaylist(long pId, String email);
//    public ResponseEntity<?> getLikedSongs(String email);
    public ResponseEntity<?> getTopCharts(String topChartsLang);
    public ResponseEntity<?> addToHistory(long id, String email);
    public ResponseEntity<?> getRecentlyPlayed(String email);
    public ResponseEntity<?> getFullHistory(String email);
    public ResponseEntity<?> viewArtist(Long id);
}
