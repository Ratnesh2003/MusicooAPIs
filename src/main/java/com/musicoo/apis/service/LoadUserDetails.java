package com.musicoo.apis.service;

import com.musicoo.apis.model.Artist;
import com.musicoo.apis.model.User;
import org.springframework.stereotype.Service;

@Service
public interface LoadUserDetails {
    public User loadUserByEmail(String email);
    public Artist loadArtistByEmail(String email);
}
