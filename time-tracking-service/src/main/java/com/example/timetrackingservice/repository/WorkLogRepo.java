package com.example.timetrackingservice.repository;

import com.example.timetrackingservice.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface WorkLogRepo extends JpaRepository<WorkLog, Long> {

    Optional<WorkLog> findTop1ByUserIdOrderByStartTimeDesc(Long userId);

    List<WorkLog> findAllByUserId(Long userId);

    List<WorkLog> findAllByStartTimeBetween(LocalDateTime from, LocalDateTime to);
    List<WorkLog> findAllByUserIdAndStartTimeBetween(Long userId, LocalDateTime from, LocalDateTime to);

}
