package com.musicoo.apis.repository;

import com.musicoo.apis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
//    User findById(long id);
    User findUserById(long id);
}
