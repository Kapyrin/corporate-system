package ru.roznov.rabbitservice.mapper;


import org.mapstruct.Mapper;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotifyMapper {
    Notification toEntity(NotifyCreateDTO notifyCreateDTO);

    List<NotifyDetailDTO> toDetailDTO(List<Notification> notifications);
    NotifyDetailDTO toDetailDTO(Notification notification);
}
