package com.musicoo.apis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class UserPlaylist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String playlistName;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private MusicooUser musicooUser;

    @ManyToMany
    @JoinColumn(name = "song_id")
    private List<Song> songs;
}
