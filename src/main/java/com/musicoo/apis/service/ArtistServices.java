package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooArtist;
import org.springframework.http.ResponseEntity;

public interface ArtistServices {
    public ResponseEntity<?> createAlbum(String name, MusicooArtist artist);
}
