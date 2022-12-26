package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.Album;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.repository.AlbumRepo;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.service.ArtistServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArtistServicesImpl implements ArtistServices {
    private final AlbumRepo albumRepo;
    private final ArtistRepo artistRepo;


    @Override
    public ResponseEntity<?> createAlbum(String name, MusicooArtist artist) {
        Album album = new Album(name, new Date(),null, artist);
        albumRepo.save(album);
        return ResponseEntity.ok().body("Album added successfully");
    }

    @Override
    public ResponseEntity<List<Album>> getAlbums(Long aId) {
        Optional<MusicooArtist> artist = artistRepo.findById(aId);
        List<Album> albums = albumRepo.findAlbumsByMusicooArtist(artist);
        return ResponseEntity.status(HttpStatus.OK).body(albums);
    }
}
