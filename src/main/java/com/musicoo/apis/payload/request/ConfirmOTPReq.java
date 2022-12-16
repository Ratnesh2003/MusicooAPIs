package com.musicoo.apis.payload.request;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class ConfirmOTPReq {
    @NotNull
    private String email;
    @NotNull
    private int otp;
    @NotNull
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain a lowercase character, an uppercase character, a special character and a number")
    private String password;
}
