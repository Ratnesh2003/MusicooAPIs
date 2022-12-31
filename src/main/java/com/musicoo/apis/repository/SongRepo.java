package com.musicoo.apis.repository;

import com.musicoo.apis.model.Genre;
import com.musicoo.apis.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SongRepo extends JpaRepository<Song, Long> {
    List<Song> findByGenre(Genre genre);
    List<Song> findSongsByGenre(Genre genre);

    Song findById(long id);
}
