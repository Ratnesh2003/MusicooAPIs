package com.musicoo.apis.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class ChoicesReq {
    private int genreCount;
    private int artistCount;
    private List<Integer> genres;
    private List<Integer> artists;
}
