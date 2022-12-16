package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.Artist;
import com.musicoo.apis.model.User;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.LoadUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoadUserDetailsImpl implements LoadUserDetails {
    private UserRepo userRepo;
    private ArtistRepo artistRepo;

    @Override
    public User loadUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Artist loadArtistByEmail(String email) {
        return artistRepo.findByEmail(email);
    }
}
