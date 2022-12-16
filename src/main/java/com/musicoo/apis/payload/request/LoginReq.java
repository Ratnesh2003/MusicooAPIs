package com.musicoo.apis.payload.request;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String password;
}
