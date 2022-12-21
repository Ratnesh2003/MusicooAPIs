package com.musicoo.apis.service.Implementation;

import com.musicoo.apis.model.MusicooArtist;
import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class ArtistDetailsImpl implements UserDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private Boolean isEnabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public static ArtistDetailsImpl build(MusicooArtist musicooArtist) {
        return new ArtistDetailsImpl(
                musicooArtist.getId(),
                musicooArtist.getFirstName(),
                musicooArtist.getLastName(),
                musicooArtist.getEmail(),
                musicooArtist.getPassword(),
                musicooArtist.getRole(),
                musicooArtist.getIsEnabled()
        );
    }
}
