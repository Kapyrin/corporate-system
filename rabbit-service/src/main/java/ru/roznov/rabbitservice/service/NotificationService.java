package ru.roznov.rabbitservice.service;

import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;

import java.util.List;

public interface NotificationService {
    NotifyDetailDTO saveNotification(NotifyCreateDTO notification);

    List<NotifyDetailDTO> getNotifications(Long userId);

    List<NotifyDetailDTO> getRecentNotifications(Long userId, int limit);
}
