package com.musicoo.apis.repository;

import com.musicoo.apis.model.ListeningHistory;
import com.musicoo.apis.model.MusicooUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListenHistoryRepo extends JpaRepository<ListeningHistory, Long> {
    ListeningHistory findByUserHistory(MusicooUser user);
    void deleteListeningHistoryByUserHistory(MusicooUser user);

 }
