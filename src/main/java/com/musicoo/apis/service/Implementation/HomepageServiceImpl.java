package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.Genre;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.Song;
import com.musicoo.apis.repository.GenreRepo;
import com.musicoo.apis.repository.SongRepo;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.HomepageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomepageServiceImpl implements HomepageService {
    private final SongRepo songRepo;
    private final GenreRepo genreRepo;
    private final UserRepo userRepo;

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
}
