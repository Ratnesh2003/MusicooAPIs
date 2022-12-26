package com.musicoo.apis.repository;

import com.musicoo.apis.model.MusicooArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepo extends JpaRepository<MusicooArtist, Long> {
    MusicooArtist findByEmailIgnoreCase(String email);
    MusicooArtist findById(long id);
    Boolean existsByEmailIgnoreCase(String email);
}
