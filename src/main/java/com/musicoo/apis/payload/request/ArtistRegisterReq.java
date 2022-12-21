package com.musicoo.apis.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ArtistRegisterReq {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
