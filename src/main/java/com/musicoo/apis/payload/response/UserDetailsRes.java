package com.musicoo.apis.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter @Setter
public class UserDetailsRes {
    private String firstName;
    private String lastName;
}
