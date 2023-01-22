package com.musicoo.apis.payload.response;

import com.musicoo.apis.model.enums.SongLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter @Setter
public class SongRes {
    private Long id;
    private String sName;
    private Date sRelease;
    private int likes;
    private SongLanguage language;
    private float duration;
    private String coverImagePath;
    private String audioPath;
    private Long artistId;
    private String artistName;
    private Boolean liked;

}
