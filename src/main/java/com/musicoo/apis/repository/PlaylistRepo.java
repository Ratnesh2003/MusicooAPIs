package com.musicoo.apis.repository;

import com.musicoo.apis.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepo extends JpaRepository<Playlist, Long> {
}
