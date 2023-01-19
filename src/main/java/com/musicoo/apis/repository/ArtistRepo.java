package com.musicoo.apis.repository;

import com.musicoo.apis.controller.Auth.Artist;
import com.musicoo.apis.model.MusicooArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistRepo extends JpaRepository<MusicooArtist, Long> {
    MusicooArtist findByEmailIgnoreCase(String email);
    MusicooArtist findById(long id);
    MusicooArtist findMusicooArtistById(long id);
    Boolean existsByEmailIgnoreCase(String email);

    @Query(value = "SELECT * from \"musicoo_artist\" order by \"ratings\" desc limit 100", nativeQuery = true)
    List<MusicooArtist> findTopTenMusicooArtists();

    @Query(value = "select \"id\", \"first_name\", \"last_name\" from \"musicoo_artist\"", nativeQuery = true)
    List<?> findAllArtists();

    List<MusicooArtist> findMusicooArtistsByFirstNameIsContainingIgnoreCase(String firstName);

}
