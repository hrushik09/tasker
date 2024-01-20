ALTER TABLE card_actions
    ADD COLUMN due TIMESTAMP;

CREATE TABLE date_actions
(
    id         INT         NOT NULL AUTO_INCREMENT,
    type       VARCHAR(45) NOT NULL,
    due_at     TIMESTAMP   NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_date_actions PRIMARY KEY (id)
);

ALTER TABLE actions
    MODIFY list_action_id INT NULL;

ALTER TABLE actions
    ADD COLUMN date_action_id INT AFTER member_creator_action_id,
    ADD CONSTRAINT FK_actions_date_action_id FOREIGN KEY (date_action_id) REFERENCES date_actions (id);