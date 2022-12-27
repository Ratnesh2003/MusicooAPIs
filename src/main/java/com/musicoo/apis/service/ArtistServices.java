package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooArtist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ArtistServices {
    public ResponseEntity<?> createAlbum(String name, MusicooArtist artist);
    public ResponseEntity<?> getAlbums(Long aId);
    public ResponseEntity<?> uploadSong(String songDetails, MultipartFile coverImage, MultipartFile audioFile, MusicooArtist artist);
}
