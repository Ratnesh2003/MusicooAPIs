package com.musicoo.apis.payload.response;

import com.musicoo.apis.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class UserInfoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String accessToken;
    private String refreshToken;

}
