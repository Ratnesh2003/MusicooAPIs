package com.musicoo.apis.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ResetPassReq {
    @Email // regex for email
    private String email;
    private String oldPassword;
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain a lowercase character, an uppercase character, a special character and a number")
    private String newPassword;
}
