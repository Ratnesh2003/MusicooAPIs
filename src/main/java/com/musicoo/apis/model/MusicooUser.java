package com.musicoo.apis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.musicoo.apis.model.enums.Provider;
import com.musicoo.apis.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "musicoo_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MusicooUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role = Role.ROLE_USER;
    private Boolean isEnabled = true;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @ManyToMany
    @JoinColumn(name = "genre_id")
    private List<Genre> likedGenres;
    @ManyToMany
    @JoinColumn(name = "artist_id")
    private List<MusicooArtist> likedArtists;


    public MusicooUser(String firstName, String lastName, String email, String password, Provider provider) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }
}
