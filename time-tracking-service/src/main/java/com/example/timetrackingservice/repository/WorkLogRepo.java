package com.example.timetrackingservice.repository;

import com.example.timetrackingservice.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface WorkLogRepo extends JpaRepository<WorkLog, Long> {
    @Query("SELECT w FROM WorkLog w WHERE w.userId = :userId ORDER BY w.startTime DESC LIMIT 1")
    Optional<WorkLog> findLastByUserId(@Param("userId") Long userId);
    List<WorkLog> findAllByUserId(Long userId);
    //todo методы для get по любому из Отрезков времени
}
