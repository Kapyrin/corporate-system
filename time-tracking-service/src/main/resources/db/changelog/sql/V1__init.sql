--liquibase formatted sql

--changeset vladimir:1
CREATE TABLE  work_logs (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id BIGINT NOT NULL,
                       start_time TIMESTAMP,
                       end_time TIMESTAMP
);

--changeset vladimir:2
INSERT INTO work_logs (user_id, start_time, end_time) VALUES
-- Alex (user_id = 1)
(1, '2025-01-02T08:00:00', '2025-01-02T17:00:00'),
(1, '2025-01-03T09:00:00', '2025-01-03T18:00:00'),
(1, '2025-01-04T08:15:00', '2025-01-04T15:45:00'),

-- Evgeniy (user_id = 2)
(2, '2025-02-10T08:00:00', '2025-02-10T16:00:00'),
(2, '2025-02-11T08:00:00', '2025-02-11T17:30:00'),

-- Vladimir (user_id = 3)
(3, '2025-03-01T10:00:00', '2025-03-01T18:00:00'),
(3, '2025-03-02T09:30:00', '2025-03-02T19:00:00'),
(3, '2025-03-03T08:45:00', '2025-03-03T17:15:00');
