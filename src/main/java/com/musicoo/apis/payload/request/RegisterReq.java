package com.musicoo.apis.payload.request;

import com.musicoo.apis.model.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterReq {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Provider provider;
}
