--liquibase formatted sql
--changeset admin:1
CREATE TABLE IF NOT EXISTS COMMENT(
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    likes_number INT,
    post_id  BIGINT NOT NULL,
    CONSTRAINT comment_user_fk
        FOREIGN KEY (owner_id) REFERENCES app_user(id),
    CONSTRAINT comment_post_fk
        FOREIGN KEY (post_id) REFERENCES post(id)

)