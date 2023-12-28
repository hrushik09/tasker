ALTER TABLE lists
    DROP COLUMN user_id;

ALTER TABLE lists
    ADD COLUMN user_id INT,
    ADD CONSTRAINT FK_lists_user_id FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE SET NULL ON UPDATE CASCADE;