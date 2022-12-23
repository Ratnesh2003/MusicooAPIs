package com.musicoo.apis.repository;

import com.musicoo.apis.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepo extends JpaRepository<Album, Long> {
}
