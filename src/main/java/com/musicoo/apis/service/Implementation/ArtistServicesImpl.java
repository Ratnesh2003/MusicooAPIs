package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.Album;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.repository.AlbumRepo;
import com.musicoo.apis.service.ArtistServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ArtistServicesImpl implements ArtistServices {
    private final AlbumRepo albumRepo;


    @Override
    public ResponseEntity<?> createAlbum(String name, MusicooArtist artist) {
        Album album = new Album(name, new Date(),null, artist);
        albumRepo.save(album);
        return ResponseEntity.ok().body("Album added successfully");

    }
}
