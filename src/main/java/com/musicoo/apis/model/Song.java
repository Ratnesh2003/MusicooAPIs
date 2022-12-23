package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sId;
    private String sName;
    private Date sRelease;
    private int likes;
    private String lyrics;
    private Long duration;
    private String sCoverImageName;
    @ManyToOne
    @JoinColumn(name = "album_id")
//    @JoinTable(name = "album", joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "a_id"))
//    @JoinColumn(name = "album_id", referencedColumnName = "a_id")
    private Album album;
    @ManyToOne
    @JoinColumn(name = "genre_id")
//    @JoinTable(name = "genre", joinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "g_id"))
//    @JoinColumn(name = "song_id", referencedColumnName = "g_id")
    private Genre genre;
    




}
