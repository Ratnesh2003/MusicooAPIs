package com.musicoo.apis.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class SongPreviewRes {
    private String songName;
    private Long songId;
    private String songImage;
    private String artistName;

}
