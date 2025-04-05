package com.example.timetrackingservice.repository;

import com.example.timetrackingservice.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface WorkLogRepo extends JpaRepository<WorkLog, Long> {
    @Query("SELECT MAX(w.id) FROM WorkLog w WHERE w.userId = :userId")
    Long findMaxIdByUserId(@Param("userId") Long userId);

    Optional<WorkLog>getWorkLogByUserIdAndId(Long userId, Long id);

    //todo методы для get по любому из Отрезков времени
}
