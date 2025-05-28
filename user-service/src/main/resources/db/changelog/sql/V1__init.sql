--liquibase formatted sql

--changeset vladimir:1
CREATE TABLE IF NOT EXISTS users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--changeset vladimir:2
INSERT INTO users (name, email) VALUES
                                    ('Alex', 'alex@example.com'),
                                    ('Evgeniy', 'evgeniy@example.com'),
                                    ('Vladimir', 'vladimir@example.com');

--changeset vladimir:3
ALTER TABLE users ALTER COLUMN name DROP NOT NULL;

--changeset vladimir:4
ALTER TABLE users ALTER COLUMN email DROP NOT NULL;
