-- V4__change_conversation_category.sql

-- conversation_category 제거
DROP TABLE conversation_category;

-- category 컬럼 변경
ALTER TABLE category
    DROP COLUMN name,
    ADD COLUMN content VARCHAR(32) NOT NULL,
    ADD COLUMN display_order INT NOT NULL;

-- conversation 테이블에 category_id 컬럼 추가
ALTER TABLE conversation
    ADD COLUMN category_id BIGINT;

-- category(id)를 참조하는 FK 제약조건 추가
ALTER TABLE conversation
    ADD CONSTRAINT fk_conversation_category
    FOREIGN KEY (category_id) REFERENCES category(id)
        ON DELETE SET NULL;
