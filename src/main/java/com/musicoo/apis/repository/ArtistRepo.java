package com.musicoo.apis.repository;

import com.musicoo.apis.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepo extends JpaRepository<Artist, Long> {
    Artist findByEmail(String email);
    Boolean existsByEmail(String email);
}
