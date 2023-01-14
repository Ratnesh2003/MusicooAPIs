package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.*;
import com.musicoo.apis.model.enums.SongLanguage;
import com.musicoo.apis.payload.request.LikedReq;
import com.musicoo.apis.repository.*;
import com.musicoo.apis.service.HomepageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomepageServiceImpl implements HomepageService {
    private final SongRepo songRepo;
    private final GenreRepo genreRepo;
    private final UserRepo userRepo;
    private final PlaylistRepo playlistRepo;
    private final ArtistRepo artistRepo;

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

    @Override
    public ResponseEntity<?> addToLiked(LikedReq likedReq, String email) {
        MusicooUser musicooUser = userRepo.findByEmailIgnoreCase(email);
        Song song = songRepo.findById(likedReq.songId());
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
    public ResponseEntity<?> getTopCharts() {
        HashMap<String, List<Song>> map = new HashMap<>();
        try {
            List<Song> topHindiSongs = songRepo.findTopHundredSongsByLikesAndLanguage(SongLanguage.HINDI);
            List<Song> topEnglishSongs = songRepo.findTopHundredSongsByLikesAndLanguage(SongLanguage.ENGLISH);
            List<Song> topPunjabiSongs = songRepo.findTopHundredSongsByLikesAndLanguage(SongLanguage.PUNJABI);
            List<Song> allTopSongs = songRepo.findTopHundredSongsByLikes();
            map.put("Top Hindi", topHindiSongs);
            map.put("Top English", topEnglishSongs);
            map.put("Top Punjabi", topPunjabiSongs);
            map.put("Al Time Top", allTopSongs);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred");
        }
    }

    public ResponseEntity<?> allArtists() {
        return ResponseEntity.status(HttpStatus.OK).body(artistRepo.findAllArtists());
    }

    public ResponseEntity<?> allGenres() {
        return ResponseEntity.status(HttpStatus.OK).body(genreRepo.findAllGenres());
    }

}
