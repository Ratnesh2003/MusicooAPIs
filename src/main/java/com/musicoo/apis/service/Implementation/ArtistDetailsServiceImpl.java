package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.repository.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArtistDetailsServiceImpl implements UserDetailsService {
    private final ArtistRepo artistRepo;
    @Override
    public ArtistDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        MusicooArtist musicooArtist = artistRepo.findByEmailIgnoreCase(email);
        if (musicooArtist != null) {
            return ArtistDetailsImpl.build(musicooArtist);
        } else {
            throw new UsernameNotFoundException("Artist with this email not found");
        }
    }
}
