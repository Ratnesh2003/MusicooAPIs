package com.musicoo.apis.service.Implementation;

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
            System.out.println(songs);
            List<Song> tempSongs = songRepo.findSongsByGenre(genre);
            System.out.println(tempSongs.toString());
            songs.addAll(tempSongs);
        }
        return ResponseEntity.status(HttpStatus.OK).body(songs);
    }

//    public ResponseEntity<?> addToPlaylist(OnlyIdReq onlyIdReq, String email) {
//        UserPlaylist userPlaylist = playlistRepo.findByPlaylistNameAndMusicooUser();
//    }

    @Override
    public ResponseEntity<?> addToLiked(OnlyIdReq onlyIdReq, String email) {
        MusicooUser musicooUser = userRepo.findByEmailIgnoreCase(email);
        Song song = songRepo.findById(onlyIdReq.songId());
        if (!playlistRepo.existsByPlaylistNameAndMusicooUser("Liked", musicooUser)) {
            UserPlaylist userPlaylist = new UserPlaylist();
            userPlaylist.setPlaylistName("Liked");
            userPlaylist.setSongs(null);
            playlistRepo.save(userPlaylist);
        }

        UserPlaylist userPlaylist = playlistRepo.findByPlaylistNameAndMusicooUser("Liked", musicooUser);
        List<Song> newSongList = userPlaylist.getSongs();
        if (!newSongList.isEmpty() && newSongList.contains(song)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Song has been liked already");
        }

        newSongList.add(song);
        userPlaylist.setSongs(newSongList);
        playlistRepo.save(userPlaylist);
        song.setLikes(song.getLikes()+1);
        songRepo.save(song);
        MusicooArtist artist = artistRepo.findMusicooArtistById(song.getArtist().getId());
        artist.setRatings(artist.getRatings()+1);
        return ResponseEntity.status(HttpStatus.OK).body("Song added to liked");

    }

    @Override
    public ResponseEntity<?> getAllPlaylists(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        List<UserPlaylist> userPlaylists = playlistRepo.findByMusicooUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userPlaylists);
    }

    @Override
    public ResponseEntity<?> getSongsOfPlaylist(long pId, String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist playlist = playlistRepo.findByIdAndMusicooUser(pId, user);
        List<Song> songs = playlist.getSongs();
        if (!songs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(songs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    public ResponseEntity<?> getLikedSongs(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist playlist = playlistRepo.findByPlaylistNameAndMusicooUser("Liked", user);
        List<Song> songs = playlist.getSongs();
        return ResponseEntity.status(HttpStatus.OK).body(songs);
    }

    @Override
    public ResponseEntity<?> getTopCharts(String nameOfChart) {
        if (Objects.equals(nameOfChart, "ALLTIME")) {
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
        List<SongPreviewRes> songPreviewRes = new ArrayList<>();
        for (Song song: allHistorySongs) {
            SongPreviewRes songPreview = new SongPreviewRes(
                    song.getSName(),
                    song.getId(),
                    song.getCoverImagePath(),
                    song.getArtist().getFirstName() + " " + song.getArtist().getLastName()
            );
            songPreviewRes.add(songPreview);
        }
        return ResponseEntity.status(HttpStatus.OK).body(songPreviewRes);
        
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

}
