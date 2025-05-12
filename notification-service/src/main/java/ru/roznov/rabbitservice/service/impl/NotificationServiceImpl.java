package ru.roznov.rabbitservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.mapper.NotifyMapper;
import ru.roznov.rabbitservice.rabbit.MessageSender;
import ru.roznov.rabbitservice.repository.NotificationRepository;
import ru.roznov.rabbitservice.service.NotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotifyMapper mapper;
    private final MessageSender messageSender;

    @Override
    public NotifyDetailDTO saveNotification(NotifyCreateDTO dto) {
        Notification savedNotification = notificationRepository.save(mapper.toEntity(dto));
        messageSender.sendToTelegram(dto);
        return mapper.toDetailDTO(savedNotification);
    }

    @Override
    public List<NotifyDetailDTO> getNotifications(Long userId) {
        return mapper.toDetailDTO(notificationRepository.findByUserId(userId));
    }

    public List<NotifyDetailDTO> getRecentNotifications(Long userId, int limit) {
        return mapper.toDetailDTO(notificationRepository.findRecentByUserId(userId, Limit.of(limit)));
    }
}
