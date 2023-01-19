package com.musicoo.apis.repository;

import com.musicoo.apis.model.Genre;
import com.musicoo.apis.model.Song;
import com.musicoo.apis.model.enums.SongLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SongRepo extends JpaRepository<Song, Long> {
    List<Song> findByGenre(Genre genre);
    List<Song> findSongsByGenre(Genre genre);

    Song findById(long id);

    @Query(value = "SELECT * from \"song\" order by \"likes\" desc limit 100", nativeQuery = true)
    List<Song> findTopHundredSongsByLikes();

    @Query(value = "SELECT * from \"song\" where \"language\" like %?1% order by \"likes\" desc limit 100", nativeQuery = true)
    List<Song> findTopHundredSongsByLikesAndLanguage(SongLanguage lang);

    @Query(value = "SELECT * from \"song\" where LOWER(\"s_name\") like LOWER(CONCAT('%',:searchText,'%'))", nativeQuery = true)
    List<Song> findSongsBySNameContainingIgnoreCase(String searchText);

    @Query(value = "SELECT * FROM \"song\" ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Song> findRandomSongs();

}
