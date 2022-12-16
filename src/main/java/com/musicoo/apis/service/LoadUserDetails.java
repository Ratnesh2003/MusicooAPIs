package com.musicoo.apis.service;

import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.MusicooUser;
import org.springframework.stereotype.Service;

@Service
public interface LoadUserDetails {
    public MusicooUser loadUserByEmail(String email);
    public MusicooArtist loadArtistByEmail(String email);
}
