--liquibase formatted sql
--changeset admin:1
CREATE TABLE IF NOT EXISTS APP_USER(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    locket BOOLEAN,
    enable BOOLEAN);