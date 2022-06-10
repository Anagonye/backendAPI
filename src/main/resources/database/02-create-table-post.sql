--liquibase formatted sql
--changeset admin:1
CREATE TABLE IF NOT EXISTS POST(
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    likes_number INT,
    owner_id BIGINT NOT NULL,
    CONSTRAINT post_owner_fk FOREIGN KEY (owner_id) REFERENCES app_user(id)
);