package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.helper.SongHelper;
import com.musicoo.apis.model.*;
import com.musicoo.apis.model.enums.SongLanguage;
import com.musicoo.apis.payload.response.SongPreviewRes;
import com.musicoo.apis.payload.response.SongRes;
import com.musicoo.apis.repository.*;
import com.musicoo.apis.service.HomepageService;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.minidev.json.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomepageServiceImpl implements HomepageService {
    private final SongRepo songRepo;
    private final GenreRepo genreRepo;
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;
    private final ListenHistoryRepo listenHistoryRepo;
    private final SongHelper songHelper;
    private final PlaylistRepo playlistRepo;

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
    public ResponseEntity<?> getTopCharts(String nameOfChart, String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        if (Objects.equals(nameOfChart.toUpperCase(), "ALLTIME")) {
            List<Song> allTopSongs = songRepo.findTopHundredSongsByLikes();
            return ResponseEntity.status(HttpStatus.OK).body(songHelper.getSongList(allTopSongs, user));
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
    @Transactional
    public ResponseEntity<?> addToHistory(long songId, String email) {
        Song song = songRepo.findById(songId);
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        ListeningHistory history = listenHistoryRepo.findByUserHistory(user);
        if (history != null) {
            List<Song> songs = history.getHistorySongs();
            songs.remove(song);
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
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        ListeningHistory history = listenHistoryRepo.findByUserHistory(user);
        List<Song> allHistorySongs = history.getHistorySongs();
        return ResponseEntity.ok().body(songHelper.getSongList(allHistorySongs.stream().limit(10).collect(Collectors.toList()), user));
    }

    @Override
    public ResponseEntity<?> getFullHistory(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        ListeningHistory history = listenHistoryRepo.findByUserHistory(user);
        List<Song> allHistorySongs = history.getHistorySongs();
        return ResponseEntity.status(HttpStatus.OK).body(songHelper.getSongList(allHistorySongs, user));
        
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

    public ResponseEntity<?> listenSong(Long id, String email) {
        Song song = songRepo.findSongById(id);
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist likedPlaylist = playlistRepo.findByPlaylistNameAndMusicooUser("Liked", user);
        boolean liked = likedPlaylist.getSongs().contains(song);
        SongRes songRes = new SongRes(
                song.getId(),
                song.getSName(),
                song.getSRelease(),
                song.getLikes(),
                song.getLanguage(),
                song.getDuration(),
                song.getCoverImagePath(),
                song.getAudioPath(),
                song.getArtist().getId(),
                song.getArtist().getFirstName() + " " + song.getArtist().getLastName(),
                liked);
        return ResponseEntity.status(HttpStatus.OK).body(songRes);
    }

    public ResponseEntity<?> searchRandomSongs(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        List<Song> songsList = songRepo.findRandomSongs();
        return ResponseEntity.ok().body(songHelper.getSongList(songsList, user));
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
        return songRepo.findSongsBySNameContainingIgnoreCase(searchText);
    }

    private List<MusicooArtist> searchArtistThroughName(String searchText) {
        return artistRepo.findMusicooArtistsByFirstNameIsContainingIgnoreCase(searchText);
    }

    public ResponseEntity<?> checkLiked(long songId, String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist likedPlaylist = playlistRepo.findByPlaylistNameAndMusicooUser("Liked", user);
        Song song = songRepo.findSongById(songId);
        return likedPlaylist != null && likedPlaylist.getSongs().contains(song)
                ? ResponseEntity.status(HttpStatus.OK).body(true)
                : ResponseEntity.status(HttpStatus.OK).body(false);
    }

    public ResponseEntity<?> getLyrics(long songId) throws IOException, InterruptedException {
        Song song = songRepo.findSongById(songId);
        String songName = song.getSName().replace(" ", "%20");
        System.out.println(songName);
        HttpRequest songIdRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://genius-song-lyrics1.p.rapidapi.com/search/?q=" + songName))
                .header("X-RapidAPI-Key", "71ad666c8bmshb0ee60aec893be6p158e71jsncbbdbccf7187")
                .header("X-RapidAPI-Host", "genius-song-lyrics1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> songIdResponse = HttpClient.newHttpClient().send(songIdRequest, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(songIdResponse.body());
        JSONArray hitsArray = json.getJSONArray("hits");
        JSONObject firstHit = hitsArray.getJSONObject(0);
        JSONObject result = firstHit.getJSONObject("result");
        long lyricSongId = result.getLong("id");

        HttpRequest songLyricsRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://genius-song-lyrics1.p.rapidapi.com/song/lyrics/?id=" + lyricSongId))
                .header("X-RapidAPI-Key", "71ad666c8bmshb0ee60aec893be6p158e71jsncbbdbccf7187")
                .header("X-RapidAPI-Host", "genius-song-lyrics1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> lyricsResponse = HttpClient.newHttpClient().send(songLyricsRequest, HttpResponse.BodyHandlers.ofString());
        JSONObject lyricsJson = new JSONObject(lyricsResponse.body());
        String lyrics = lyricsJson.getJSONObject("lyrics").getJSONObject("lyrics").getJSONObject("body").getString("html");


        return ResponseEntity.ok().body(lyrics);
    }



}
