CREATE TABLE relationship (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(50) NOT NULL,
    display_order INT NOT NULL
);

CREATE TABLE conversation_relationship (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    relationship_id bigint NOT NULL,
    conversation_id bigint NOT NULL,
    FOREIGN KEY (relationship_id) REFERENCES relationship (id),
    FOREIGN KEY (conversation_id) REFERENCES conversation (id)
);

ALTER TABLE conversation
DROP COLUMN relationship;

INSERT INTO relationship (content, display_order) VALUES
('연인', 1),
('친구', 2),
('가족 (부모님)', 3),
('가족 (형제관계)', 4),
('지인', 5),
('첫만남', 6)
;
