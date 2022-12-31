package com.musicoo.apis.repository;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepo extends JpaRepository<UserPlaylist, Long> {
    UserPlaylist findByPlaylistNameAndMusicooUser(String name, MusicooUser musicooUser);
    Boolean existsByPlaylistNameAndMusicooUser(String name, MusicooUser musicooUser);
    UserPlaylist findByIdAndMusicooUser(long id, MusicooUser musicooUser);
}
