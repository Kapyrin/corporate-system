--liquibase formatted sql

--changeset vladimir:1
CREATE TABLE  work_logs (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id BIGINT NOT NULL,
                       start_time TIMESTAMP,
                       end_time TIMESTAMP
);
