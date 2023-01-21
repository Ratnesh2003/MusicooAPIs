package com.musicoo.apis.repository;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.payload.response.UserDetailsRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<MusicooUser, Long> {
    MusicooUser findByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);

    @Query(value = "select \"id\", \"first_name\", \"last_name\" from \"musicoo_user\" where LOWER(\"email\") = LOWER(?1)", nativeQuery = true)
    List<?> findLimitedByEmail(String email);
}
