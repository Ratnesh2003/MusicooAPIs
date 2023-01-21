package com.musicoo.apis.repository;

import com.musicoo.apis.model.Album;
import com.musicoo.apis.model.MusicooArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepo extends JpaRepository<Album, Long> {
    List<Album> findAlbumsByMusicooArtist(Optional<MusicooArtist> artist);

    Album findByIdAndMusicooArtist(long id, MusicooArtist artist);
}
