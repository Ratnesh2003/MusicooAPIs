package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class DefaultPlaylist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String playlistName;

    @ManyToMany
    @JoinColumn(name = "song_id")
    private List<Song> defaultSongs;
}
