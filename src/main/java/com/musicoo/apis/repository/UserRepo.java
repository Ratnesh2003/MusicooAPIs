package com.musicoo.apis.repository;

import com.musicoo.apis.model.MusicooUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<MusicooUser, Long> {
    MusicooUser findByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);
}
