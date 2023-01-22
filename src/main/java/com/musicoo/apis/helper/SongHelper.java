package com.musicoo.apis.helper;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.Song;
import com.musicoo.apis.model.UserPlaylist;
import com.musicoo.apis.payload.response.SongRes;
import com.musicoo.apis.repository.PlaylistRepo;
import com.musicoo.apis.repository.SongRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SongHelper {
    private final PlaylistRepo playlistRepo;


    public List<SongRes> getSongList(List<Song> originalSongList, MusicooUser user) {
        List<SongRes> songResList = new ArrayList<>();
        for (Song song: originalSongList){
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
            songResList.add(songRes);
        }
        return songResList;
    }
}
