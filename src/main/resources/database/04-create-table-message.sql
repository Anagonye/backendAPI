--liquibase formatted sql
--changeset admin:1

CREATE TABLE IF NOT EXISTS MESSAGE(
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    send_time TIMESTAMP NOT NULL,
    display_time TIMESTAMP,
    CONSTRAINT message_sender_fk FOREIGN KEY (sender_id) REFERENCES app_user(id),
    CONSTRAINT message_receive_fk FOREIGN KEY (receiver_id) REFERENCES app_user(id)
);