package ru.roznov.rabbitservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.entity.NotificationType;

import java.util.List;

import static ru.roznov.rabbitservice.entity.NotificationType.WORK_DAY_ENDED;
import static ru.roznov.rabbitservice.entity.NotificationType.WORK_DAY_STARTED;

@Mapper(componentModel = "spring", imports = ru.roznov.rabbitservice.entity.NotificationType.class)
public interface NotifyMapper {

    @Mapping(
            target = "message",
            expression = "java(getMessageByType(notifyCreateDTO.getType()))"
    )
    Notification toEntity(NotifyCreateDTO notifyCreateDTO);

    List<NotifyDetailDTO> toDetailDTO(List<Notification> notifications);
    NotifyDetailDTO toDetailDTO(Notification notification);

    default String getMessageByType(NotificationType type) {
        return switch (type) {
            case WORK_DAY_STARTED -> "Workday started";
            case WORK_DAY_ENDED -> "Workday ended";
        };
    }
}
