package com.example.timetrackingservice.repository;

import com.example.timetrackingservice.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface WorkLogRepo extends JpaRepository<WorkLog, Long> {
    Optional<WorkLog> findTop1ByUserIdOrderByStartTimeDesc(Long userId);

    List<WorkLog> findAllByUserId(Long userId);

    List<WorkLog> findAllByStartTimeBetween(LocalDateTime from, LocalDateTime to);

}
