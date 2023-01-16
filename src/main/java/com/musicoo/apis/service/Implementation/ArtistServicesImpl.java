package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.Album;
import com.musicoo.apis.model.Genre;
import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.Song;
import com.musicoo.apis.model.enums.SongLanguage;
import com.musicoo.apis.repository.AlbumRepo;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.GenreRepo;
import com.musicoo.apis.repository.SongRepo;
import com.musicoo.apis.service.AmazonClient;
import com.musicoo.apis.service.ArtistServices;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArtistServicesImpl implements ArtistServices {
    private final AlbumRepo albumRepo;
    private final ArtistRepo artistRepo;
    private final AmazonClient amazonClient;
    private final SongRepo songRepo;
    private final GenreRepo genreRepo;


    @Override
    public ResponseEntity<?> createAlbum(String name, MusicooArtist artist) {
        Album album = new Album(name, new Date(),null, artist);
        albumRepo.save(album);
        return ResponseEntity.ok().body("Album added successfully");
    }

    @Override
    public ResponseEntity<List<Album>> getAlbums(Long aId) {
        Optional<MusicooArtist> artist = artistRepo.findById(aId);
        List<Album> albums = albumRepo.findAlbumsByMusicooArtist(artist);
        return ResponseEntity.status(HttpStatus.OK).body(albums);
    }

    @Override
    public ResponseEntity<?> uploadSong(String songDetails, MultipartFile coverImage, MultipartFile audioFile, MusicooArtist artist) {
        JsonObject jsonSongDetails = JsonParser.parseString(songDetails).getAsJsonObject();

        Album album = albumRepo.findById(Long.parseLong(jsonSongDetails.get("albumId").toString()));
        Genre genre = genreRepo.findById(Long.parseLong(jsonSongDetails.get("genreId").toString()));

        Song newSong = new Song();

        try {
            String language = jsonSongDetails.get("language").toString().trim().toLowerCase();

            switch (language.substring(1, language.length() - 1)) {
                case "hindi" -> newSong.setLanguage(SongLanguage.HINDI);
                case "english" -> newSong.setLanguage(SongLanguage.ENGLISH);
                case "punjabi" -> newSong.setLanguage(SongLanguage.PUNJABI);
                case "spanish" -> newSong.setLanguage(SongLanguage.SPANISH);
                case "russian" -> newSong.setLanguage(SongLanguage.RUSSIAN);
                case "instrumental" -> newSong.setLanguage(SongLanguage.INSTRUMENTAL);
                case "french" -> newSong.setLanguage(SongLanguage.FRENCH);
                case "miscellaneous" -> newSong.setLanguage(SongLanguage.MISCELLANEOUS);
            }
            String name = jsonSongDetails.get("name").toString();

            newSong.setSName(name.substring(1, name.length()-1));
            newSong.setSRelease(new Date());
            newSong.setLikes(0);
            newSong.setDuration(amazonClient.getDuration(audioFile));
            newSong.setCoverImagePath(amazonClient.uploadFile(coverImage));
            newSong.setAudioPath(amazonClient.uploadFile(audioFile));
            newSong.setArtist(artist);
            newSong.setAlbum(album);
            newSong.setGenre(genre);
            System.out.println(language + " " + name);

            songRepo.save(newSong);
            return ResponseEntity.status(HttpStatus.OK).body("Song uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
