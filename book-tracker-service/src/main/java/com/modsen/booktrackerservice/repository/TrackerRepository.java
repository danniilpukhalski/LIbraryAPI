package com.modsen.booktrackerservice.repository;


import com.modsen.booktrackerservice.domain.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long> {

    Optional<Tracker> findTrackerByBookId(Long bookId);
    List<Tracker> findTrackerByStatus(String status);
}
