package com.musicoo.apis.repository;

import com.musicoo.apis.model.MusicooUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<MusicooUser, Long> {
    MusicooUser findByEmail(String email);
    Boolean existsByEmail(String email);
//    MusicooUser findById(long id);
    MusicooUser findUserById(long id);
}
