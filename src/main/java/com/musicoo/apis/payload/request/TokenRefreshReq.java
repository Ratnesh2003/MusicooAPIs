package com.musicoo.apis.payload.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenRefreshReq {
    @NotNull
    private String refreshToken;

}
