package ru.roznov.rabbitservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.exception.NotificationNotFoundException;
import ru.roznov.rabbitservice.mapper.NotifyMapper;
import ru.roznov.rabbitservice.rabbit.MessageSender;
import ru.roznov.rabbitservice.repository.NotificationRepository;
import ru.roznov.rabbitservice.service.NotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotifyMapper mapper;
    private final MessageSender messageSender;

    @Override
    public NotifyDetailDTO saveNotification(NotifyCreateDTO dto) {
        try {
            Notification savedNotification = notificationRepository.save(mapper.toEntity(dto));
            messageSender.sendToTelegram(dto);
            return mapper.toDetailDTO(savedNotification);
        } catch (DataAccessException e) {
            log.error("Database error: {}", e.getMessage());
            throw new ServiceException("Failed to save notification", e);
        }
    }

    public List<NotifyDetailDTO> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            throw new NotificationNotFoundException(userId);
        }
        return mapper.toDetailDTO(notifications);
    }

    public List<NotifyDetailDTO> getRecentNotifications(Long userId, int limit) {
        return mapper.toDetailDTO(notificationRepository.findRecentByUserId(userId, Limit.of(limit)));
    }
}
