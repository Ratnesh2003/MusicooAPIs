package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.*;

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

    public MusicooUser(String firstName, String lastName, String email, String password, Provider provider) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }
}
