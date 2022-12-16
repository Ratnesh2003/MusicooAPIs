package com.musicoo.apis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ArtistEmailOTPConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;
    private String confirmationToken;
    private Date tokenCreationDate;
    private int otp;
    private Date otpCreationDate;

    public ArtistEmailOTPConfirmation(Artist artist, String confirmationToken, Date tokenCreationDate, int otp, Date otpCreationDate) {
        this.artist = artist;
        this.confirmationToken = confirmationToken;
        this.tokenCreationDate = tokenCreationDate;
        this.otp = otp;
        this.otpCreationDate = otpCreationDate;
    }

}
