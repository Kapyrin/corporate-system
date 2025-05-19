package ru.roznov.rabbitservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.roznov.rabbitservice.dto.NotificationMessage;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.entity.NotificationType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotifyMapper {

    @Mapping(source = "type", target = "message", qualifiedByName = "getMessageByType")
    Notification toEntity(NotifyCreateDTO notifyCreateDTO);

    List<NotifyDetailDTO> toDetailDTO(List<Notification> notifications);
    NotifyDetailDTO toDetailDTO(Notification notification);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "receivedAt", target = "timestamp")
    @Mapping(source = "type", target = "message", qualifiedByName = "getMessageByType")
    NotificationMessage toTelegramMessage(NotifyCreateDTO notifyCreateDTO);

    @Named("getMessageByType")
    default String getMessageByType(NotificationType type) {
        if (type == null) {
            return "Unknown notification";
        }
        
        return switch (type) {
            case WORK_DAY_STARTED -> "Workday started";
            case WORK_DAY_ENDED -> "Workday ended";

            default -> "Unsupported notification type: " + type;
        };
    }
}