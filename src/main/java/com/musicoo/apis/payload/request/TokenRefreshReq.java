package com.musicoo.apis.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class TokenRefreshReq {
    @NotNull
    private String refreshToken;

}
