package ru.roznov.rabbitservice.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.roznov.rabbitservice.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(long id);

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.receivedAt DESC")
    List<Notification> findRecentByUserId(
            @Param("userId") Long userId,
            Limit limit
    );
}
