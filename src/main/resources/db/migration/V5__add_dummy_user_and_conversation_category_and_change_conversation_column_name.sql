-- V5__add_dummy_user_and_conversation_category_and_change_conversation_column_name.sql

-- dummy user 추가
INSERT INTO users
(name, email, birthdate, created_at, updated_at, provider, provider_id, gender, profile_image_url, profile_completed)
VALUES
    ('더미 사용자', 'dummy.user@example.com', '1995-01-01', NOW(), NOW(), 'LOCAL', 'dev-001', 'MALE', 'https://picsum.photos/seed/dummy/200', 1);

-- 미리 설정된 category 추가
INSERT INTO category (content, display_order) VALUES
                                                  ('가벼운 친목', 1),
                                                  ('자기 탐색', 2),
                                                  ('관계 심화', 3),
                                                  ('감정과 기억', 4),
                                                  ('추억', 5),
                                                  ('철학적 고찰', 6);

-- conversation 테이블의 기존 FK 제약조건 제거
ALTER TABLE conversation DROP FOREIGN KEY conversation_ibfk_1;

-- conversation 테이블의 creator_user_id 컬럼 이름을 user_id로 변경
ALTER TABLE conversation
    CHANGE COLUMN creator_user_id user_id BIGINT NOT NULL;

-- conversation 테이블에 외래 키 다시 추가
ALTER TABLE conversation
    ADD CONSTRAINT fk_conversation_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE RESTRICT ON UPDATE CASCADE;
