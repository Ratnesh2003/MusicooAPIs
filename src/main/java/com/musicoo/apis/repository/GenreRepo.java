package com.musicoo.apis.repository;

import com.musicoo.apis.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findById(long id);
}
