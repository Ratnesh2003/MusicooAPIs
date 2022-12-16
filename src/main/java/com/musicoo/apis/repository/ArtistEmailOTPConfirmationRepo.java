package com.musicoo.apis.repository;

import com.musicoo.apis.model.ArtistEmailOTPConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistEmailOTPConfirmationRepo extends JpaRepository<ArtistEmailOTPConfirmation,Long> {
}
