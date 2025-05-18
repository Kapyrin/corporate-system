package ru.roznov.rabbitservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.entity.NotificationType;
import ru.roznov.rabbitservice.mapper.NotifyMapper;
import ru.roznov.rabbitservice.rabbit.MessageSender;
import ru.roznov.rabbitservice.repository.NotificationRepository;
import ru.roznov.rabbitservice.service.impl.NotificationServiceImpl;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private MessageSender messageSender;

    @Mock
    private NotifyMapper mapper;

    @InjectMocks
    private NotificationServiceImpl service;

    @Test
    void saveNotification_MapsAllFieldsCorrectly() {
        NotifyCreateDTO dto = new NotifyCreateDTO(
                123L,
                NotificationType.WORK_DAY_ENDED,
                Instant.parse("2023-01-01T18:00:00Z")
        );

        Notification entity = new Notification(
                null,
                123L,
                NotificationType.WORK_DAY_ENDED,
                "Work ended",
                Instant.parse("2023-01-01T18:00:00Z")
        );

        NotifyDetailDTO expectedDto = new NotifyDetailDTO(
                1,
                123L,
                NotificationType.WORK_DAY_ENDED,
                "Work ended",
                Instant.parse("2023-01-01T18:00:00Z")
        );

        when(mapper.toEntity(dto)).thenReturn(entity);
        entity.setId(1L);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDetailDTO(entity)).thenReturn(expectedDto);

        NotifyDetailDTO result = service.saveNotification(dto);

        assertThat(result)
                .extracting(
                        NotifyDetailDTO::getUserId,
                        NotifyDetailDTO::getType,
                        NotifyDetailDTO::getMessage
                )
                .containsExactly(
                        123L,
                        NotificationType.WORK_DAY_ENDED,
                        "Work ended"
                );
    }
}
