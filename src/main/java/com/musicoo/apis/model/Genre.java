package com.musicoo.apis.model;

import jakarta.persistence.*;

@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gId;
    private String gName;
    @ManyToOne
    @JoinColumn(referencedColumnName = "s_id")
    private Song song;
}
