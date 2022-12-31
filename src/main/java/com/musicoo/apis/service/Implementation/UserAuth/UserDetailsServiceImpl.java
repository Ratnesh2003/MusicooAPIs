package com.musicoo.apis.service.Implementation.UserAuth;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        MusicooUser musicooUser = userRepo.findByEmailIgnoreCase(email);
        if (musicooUser != null) {
            return UserDetailsImpl.build(musicooUser);
        } else {
            throw new UsernameNotFoundException("Artist with this email not found");
        }
    }
}
