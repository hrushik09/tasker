CREATE TABLE boards
(
    id         INT          NOT NULL AUTO_INCREMENT,
    title      VARCHAR(256) NOT NULL,
    user_id    INT,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_boards PRIMARY KEY (id),
    CONSTRAINT FK_boards_user_id FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE SET NULL ON UPDATE CASCADE
);