package com.musicoo.apis.service.Implementation;

import com.amazonaws.Response;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.Song;
import com.musicoo.apis.model.UserPlaylist;
import com.musicoo.apis.payload.request.OnlyIdReq;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.PlaylistRepo;
import com.musicoo.apis.repository.SongRepo;
import com.musicoo.apis.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlaylistService {
    private final UserRepo userRepo;
    private final PlaylistRepo playlistRepo;
    private final SongRepo songRepo;
    private final ArtistRepo artistRepo;

    public ResponseEntity<?> createPlaylist(String email, String nameOfPlaylist){
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        if (playlistRepo.existsByPlaylistNameIgnoreCaseAndMusicooUser(nameOfPlaylist, user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Playlist with this name already exists");
        }
        UserPlaylist playlist = new UserPlaylist();
        playlist.setPlaylistName(nameOfPlaylist);
        playlist.setMusicooUser(user);
        playlist.setSongs(null);
        playlistRepo.save(playlist);
        return ResponseEntity.status(HttpStatus.CREATED).body("Playlist created successfully");
    }

    public ResponseEntity<?> addToPlaylist(long songId, Long playlistId, String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist playlist = playlistRepo.findByIdAndMusicooUser(playlistId, user);
        List<Song> songs = playlist.getSongs();
        Song song = songRepo.findById(songId);
        songs.add(song);
        playlist.setSongs(songs);
        playlistRepo.save(playlist);
        return ResponseEntity.status(HttpStatus.OK).body("Song added to playlist successfully");
    }

    public ResponseEntity<?> deletePlaylist(Long playlistId, String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist playlist = playlistRepo.findByIdAndMusicooUser(playlistId, user);
        playlistRepo.deleteByMusicooUserAndId(user, playlistId);
        return ResponseEntity.status(HttpStatus.OK).body("Playlist deleted successfully");
    }

    public ResponseEntity<?> getAllPlaylists(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        List<UserPlaylist> userPlaylists = playlistRepo.findByMusicooUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userPlaylists);
    }

    public ResponseEntity<?> getLikedSongs(String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist playlist = playlistRepo.findByPlaylistNameAndMusicooUser("Liked", user);
        List<Song> songs = playlist.getSongs();
        return ResponseEntity.status(HttpStatus.OK).body(songs);
    }

    public ResponseEntity<?> getSongsOfPlaylist(long pId, String email) {
        MusicooUser user = userRepo.findByEmailIgnoreCase(email);
        UserPlaylist playlist = playlistRepo.findByIdAndMusicooUser(pId, user);
        List<Song> songs = playlist.getSongs();
        if (!songs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(songs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public ResponseEntity<?> addToLiked(OnlyIdReq onlyIdReq, String email) {
        MusicooUser musicooUser = userRepo.findByEmailIgnoreCase(email);
        Song song = songRepo.findById(onlyIdReq.songId());
        if (!playlistRepo.existsByPlaylistNameIgnoreCaseAndMusicooUser("Liked", musicooUser)) {
            UserPlaylist userPlaylist = new UserPlaylist();
            userPlaylist.setPlaylistName("Liked");
            userPlaylist.setSongs(null);
            userPlaylist.setMusicooUser(musicooUser);
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

}
