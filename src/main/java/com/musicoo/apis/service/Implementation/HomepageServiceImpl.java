package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.controller.Auth.Artist;
import com.musicoo.apis.model.*;
import com.musicoo.apis.model.enums.SongLanguage;
import com.musicoo.apis.payload.request.OnlyIdReq;
import com.musicoo.apis.payload.response.SongPreviewRes;
import com.musicoo.apis.repository.*;
import com.musicoo.apis.service.HomepageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomepageServiceImpl implements HomepageService {
    private final SongRepo songRepo;
    private final GenreRepo genreRepo;
    private final UserRepo userRepo;
    private final PlaylistRepo playlistRepo;
    private final ArtistRepo artistRepo;
    private final ListenHistoryRepo listenHistoryRepo;

    @Override
    public ResponseEntity<?> quickPicks(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        List<Genre> genres = user.getLikedGenres();
        List<Song> songs = new ArrayList<>();
        for (Genre genre : genres) {
            List<Song> tempSongs = songRepo.findSongsByGenre(genre);
            songs.addAll(tempSongs);
        }
        return ResponseEntity.status(HttpStatus.OK).body(songs);
    }
    @Override
    public ResponseEntity<?> getTopCharts(String nameOfChart) {
        if (Objects.equals(nameOfChart.toUpperCase(), "ALLTIME")) {
            List<Song> allTopSongs = songRepo.findTopHundredSongsByLikes();
            return ResponseEntity.status(HttpStatus.OK).body(allTopSongs);
        }
        try {
            SongLanguage lang = SongLanguage.valueOf(nameOfChart.toUpperCase());
            List<Song> topSongsChart = songRepo.findTopHundredSongsByLikesAndLanguage(lang);
            return ResponseEntity.status(HttpStatus.OK).body(topSongsChart);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred");
        }
    }

    @Override
    public ResponseEntity<?> addToHistory(long songId, String email) {
        Song song = songRepo.findById(songId);
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        ListeningHistory history = listenHistoryRepo.findByUserHistory(user);
        if (history != null) {
            List<Song> songs = history.getHistorySongs();
            songs.add(song);
            history.setHistorySongs(songs);
            listenHistoryRepo.save(history);
        } else {
            List<Song> songs = new ArrayList<>();
            songs.add(song);
            ListeningHistory newHistory = new ListeningHistory(user, songs);
            listenHistoryRepo.save(newHistory);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Song added to history");
    }

    @Override
    public ResponseEntity<?> getRecentlyPlayed(String email) {
        List<SongPreviewRes> songPreviewRes = (List<SongPreviewRes>) getFullHistory(email).getBody();
        assert songPreviewRes != null;
        return ResponseEntity.ok().body(songPreviewRes.stream().limit(10).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getFullHistory(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        ListeningHistory history = listenHistoryRepo.findByUserHistory(user);
        List<Song> allHistorySongs = history.getHistorySongs();
        return ResponseEntity.status(HttpStatus.OK).body(allHistorySongs);
        
    }

    public ResponseEntity<?> allArtists() {
        return ResponseEntity.status(HttpStatus.OK).body(artistRepo.findAllArtists());
    }

    public ResponseEntity<?> allGenres() {
        return ResponseEntity.status(HttpStatus.OK).body(genreRepo.findAllGenres());
    }

    public ResponseEntity<?> topArtists() {
        return ResponseEntity.status(HttpStatus.OK).body(artistRepo.findTopTenMusicooArtists());
    }

    @Override
    public ResponseEntity<?> viewArtist(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(artistRepo.findMusicooArtistById(id));
    }

    public ResponseEntity<?> listenSong(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(songRepo.findById(id));
    }

    public ResponseEntity<?> searchRandomSongs() {
        return ResponseEntity.ok().body(songRepo.findRandomSongs());
    }

    public ResponseEntity<?> search(String searchText) {
        List<Song> songs = searchThroughSongName(searchText);
        if (songs.isEmpty()) {
            List<MusicooArtist> artists = searchArtistThroughName(searchText);
            return ResponseEntity.ok().body(artists);
        }
        return ResponseEntity.ok().body(songs);

    }

    private List<Song> searchThroughSongName(String searchText) {
        System.out.println("Error");
        return songRepo.findSongsBySNameContainingIgnoreCase(searchText);
    }

    private List<MusicooArtist> searchArtistThroughName(String searchText) {
        System.out.println("Error2");
        return artistRepo.findMusicooArtistsByFirstNameIsContainingIgnoreCase(searchText);
    }



}
