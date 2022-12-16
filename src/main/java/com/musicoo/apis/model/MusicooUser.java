package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
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

}
