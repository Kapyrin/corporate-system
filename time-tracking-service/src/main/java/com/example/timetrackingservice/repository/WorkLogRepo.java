package com.example.timetrackingservice.repository;

import com.example.timetrackingservice.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface WorkLogRepo extends JpaRepository<WorkLog, Long> {
    @Query("SELECT w FROM WorkLog w WHERE w.userId = :userId ORDER BY w.startTime DESC LIMIT 1")
    Optional<WorkLog> findLastByUserId(@Param("userId") Long userId);
    List<WorkLog> findAllByUserId(Long userId);

    @Query("select w from WorkLog w where DATE(w.startTime) = :day")
    List<WorkLog> findAllByDate(@Param("day") LocalDate day);

    @Query("select w from WorkLog w where year(w.startTime) = :year")
    List<WorkLog> findAllByYear(@Param("year") Integer year);

    @Query("select w from WorkLog w where MONTH(w.startTime) = :month and  YEAR(w.startTime) = :year")
    List<WorkLog> findAllByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

}
