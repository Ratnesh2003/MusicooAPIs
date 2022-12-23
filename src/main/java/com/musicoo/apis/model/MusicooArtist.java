package com.musicoo.apis.model;

import com.musicoo.apis.model.enums.Provider;
import com.musicoo.apis.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "musicoo_artist")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MusicooArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role = Role.ROLE_ARTIST;
    private Boolean isEnabled = true;
    @Enumerated(EnumType.STRING)
    private Provider provider;

    public MusicooArtist(String firstName, String lastName, String email, String password, Provider provider) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }
}
