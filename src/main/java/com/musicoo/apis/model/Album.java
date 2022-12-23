package com.musicoo.apis.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
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


}
