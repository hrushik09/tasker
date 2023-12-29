ALTER TABLE lists
    DROP FOREIGN KEY FK_lists_user_id;
ALTER TABLE lists
    DROP COLUMN user_id;
ALTER TABLE lists
    ADD COLUMN board_id INT,
    ADD CONSTRAINT FK_lists_board_id FOREIGN KEY (board_id) REFERENCES boards (id)
        ON DELETE SET NULL ON UPDATE CASCADE;