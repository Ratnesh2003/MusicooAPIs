package com.musicoo.apis.repository;

import com.musicoo.apis.model.ListeningHistory;
import com.musicoo.apis.model.MusicooUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenHistoryRepo extends JpaRepository<ListeningHistory, Long> {
    ListeningHistory findByUserHistory(MusicooUser user);
 }
