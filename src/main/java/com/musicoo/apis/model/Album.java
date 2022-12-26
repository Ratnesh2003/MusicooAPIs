package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aId;
    private String aName;
    private Date aRelease;
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Song> songs;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private MusicooArtist musicooArtist;

    public Album(String aName, Date aRelease, List<Song> songs, MusicooArtist artist) {
        this.aName = aName;
        this.aRelease = aRelease;
        this.songs = songs;
        this.musicooArtist = artist;
    }


}
