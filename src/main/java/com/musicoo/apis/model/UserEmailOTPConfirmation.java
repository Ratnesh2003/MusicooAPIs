package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEmailOTPConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String confirmationToken;
    private Date tokenCreationDate;
    private int otp;
    private Date otpCreationDate;

    public UserEmailOTPConfirmation(User user, String confirmationToken, Date tokenCreationDate, int otp, Date otpCreationDate) {
        this.user = user;
        this.confirmationToken = confirmationToken;
        this.tokenCreationDate = tokenCreationDate;
        this.otp = otp;
        this.otpCreationDate = otpCreationDate;
    }
}
