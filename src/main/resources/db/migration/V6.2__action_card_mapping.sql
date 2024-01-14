ALTER TABLE actions
    ADD COLUMN card_id INT NOT NULL AFTER id,
    ADD CONSTRAINT FK_actions_card_id FOREIGN KEY (card_id) REFERENCES cards (id);