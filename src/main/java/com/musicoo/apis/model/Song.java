package com.musicoo.apis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musicoo.apis.model.enums.SongLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sName;
    private Date sRelease;
    private int likes;
    private SongLanguage language;
//    private String lyrics;
    private float duration;
    private String coverImagePath;
    private String audioPath;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private MusicooArtist artist;
    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonBackReference
    private Album album;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonBackReference
    private Genre genre;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserPlaylist> playlists;

    @ManyToMany(mappedBy = "defaultSongs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DefaultPlaylist> defaultPlaylists;
    




}
