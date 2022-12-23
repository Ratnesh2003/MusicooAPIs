package com.musicoo.apis.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gId;
    private String gName;
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<Song> songs;
}
