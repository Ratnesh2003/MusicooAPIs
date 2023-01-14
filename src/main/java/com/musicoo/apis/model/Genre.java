package com.musicoo.apis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String gName;
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Song> songs;
    @ManyToMany(mappedBy = "likedGenres", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MusicooUser> musicooUsers;
}
