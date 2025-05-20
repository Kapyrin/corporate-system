package ru.roznov.rabbitservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.roznov.rabbitservice.entity.Notification;
import ru.roznov.rabbitservice.entity.NotificationType;
import ru.roznov.rabbitservice.repository.NotificationRepository;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class NotificationRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private NotificationRepository repository;

    @Test
    void savesAndRetrievesNotificationCorrectly() {
        Notification entity = new Notification(
                null,
                456L,
                NotificationType.WORK_DAY_ENDED,
                "Work ended at 18:00",
                Instant.parse("2023-01-01T18:00:00Z")
        );

        Notification saved = repository.save(entity);
        Notification found = repository.findById(saved.getId()).orElseThrow();

        assertThat(found)
                .extracting(
                        Notification::getUserId,
                        Notification::getType,
                        Notification::getMessage
                )
                .containsExactly(
                        456L,
                        NotificationType.WORK_DAY_ENDED,
                        "Work ended at 18:00"
                );
    }
}