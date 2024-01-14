CREATE TABLE card_actions
(
    id         INT          NOT NULL AUTO_INCREMENT,
    type       VARCHAR(45)  NOT NULL,
    card_id    INT          NOT NULL,
    text       VARCHAR(256) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_card_actions PRIMARY KEY (id)
);

CREATE TABLE list_actions
(
    id         INT          NOT NULL AUTO_INCREMENT,
    type       VARCHAR(45)  NOT NULL,
    list_id    INT          NOT NULL,
    text       VARCHAR(256) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_list_actions PRIMARY KEY (id)
);

CREATE TABLE member_creator_actions
(
    id         INT          NOT NULL AUTO_INCREMENT,
    type       VARCHAR(45)  NOT NULL,
    creator_id INT          NOT NULL,
    text       VARCHAR(256) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_member_creator_actions PRIMARY KEY (id)
);

CREATE TABLE actions
(
    id                       INT          NOT NULL AUTO_INCREMENT,
    member_creator_id        INT          NOT NULL,
    type                     VARCHAR(45)  NOT NULL,
    happened_at              TIMESTAMP    NOT NULL,
    translation_key          VARCHAR(256) NOT NULL,
    card_action_id           INT          NOT NULL,
    list_action_id           INT          NOT NULL,
    member_creator_action_id INT          NOT NULL,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_actions PRIMARY KEY (id),
    CONSTRAINT FK_actions_card_action_id FOREIGN KEY (card_action_id) REFERENCES card_actions (id),
    CONSTRAINT FK_actions_list_action_id FOREIGN KEY (list_action_id) REFERENCES list_actions (id),
    CONSTRAINT FK_actions_member_creator_action_id FOREIGN KEY (member_creator_action_id) REFERENCES member_creator_actions (id)
);