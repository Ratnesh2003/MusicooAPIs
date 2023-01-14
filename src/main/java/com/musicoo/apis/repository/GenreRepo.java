package com.musicoo.apis.repository;

import com.musicoo.apis.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findById(long id);

    @Query(value = "select \"id\", \"g_name\" from \"genre\"", nativeQuery = true)
    List<?> findAllGenres();


}
