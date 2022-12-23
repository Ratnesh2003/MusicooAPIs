package com.musicoo.apis.repository;

import com.musicoo.apis.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SongRepo extends JpaRepository<Song, Long> {
}
