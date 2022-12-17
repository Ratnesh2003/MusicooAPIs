package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.repository.ArtistRepo;
import com.musicoo.apis.repository.UserRepo;
import com.musicoo.apis.service.LoadUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoadUserDetailsImpl implements LoadUserDetails {
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;

    @Override
    public MusicooUser loadUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public MusicooArtist loadArtistByEmail(String email) {
        return artistRepo.findByEmail(email);
    }
}
