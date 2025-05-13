package ru.roznov.rabbitservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.roznov.rabbitservice.dto.NotificationMessage;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.entity.NotificationType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotifyMapper {

    @Mapping(
            target = "message",
            expression = "java(getMessageByType(notifyCreateDTO.getType()))"
    )
    Notification toEntity(NotifyCreateDTO notifyCreateDTO);

    List<NotifyDetailDTO> toDetailDTO(List<Notification> notifications);
    NotifyDetailDTO toDetailDTO(Notification notification);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "timestamp", source = "receivedAt")
    @Mapping(target = "message", expression = "java(getMessageByType(notifyCreateDTO.getType()))")
    NotificationMessage toTelegramMessage(NotifyCreateDTO notifyCreateDTO);

    default String getMessageByType(NotificationType type) {
        return switch (type) {
            case WORK_DAY_STARTED -> "Workday started";
            case WORK_DAY_ENDED -> "Workday ended";
        };
    }
}
