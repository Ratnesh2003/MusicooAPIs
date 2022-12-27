package com.musicoo.apis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;
    private String aName;
    private Date aRelease;
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Song> songs;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private MusicooArtist musicooArtist;

    public Album(String aName, Date aRelease, List<Song> songs, MusicooArtist artist) {
        this.aName = aName;
        this.aRelease = aRelease;
        this.songs = songs;
        this.musicooArtist = artist;
    }


}
