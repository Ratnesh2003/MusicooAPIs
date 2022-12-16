package com.musicoo.apis.repository;

import com.musicoo.apis.model.UserEmailOTPConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmailOTPConfirmationRepo extends JpaRepository<UserEmailOTPConfirmation, Long> {
    UserEmailOTPConfirmation findByConfirmationToken(String token);
}
