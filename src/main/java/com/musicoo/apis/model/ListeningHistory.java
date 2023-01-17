package com.musicoo.apis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class ListeningHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private MusicooUser userHistory;

    @ManyToMany
    @JoinColumn(name = "song_id")
    private List<Song> historySongs;

    public ListeningHistory(MusicooUser userHistory, List<Song> historySongs) {
        this.userHistory = userHistory;
        this.historySongs = historySongs;
    }

}
