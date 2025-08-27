-- V2__remove_unused_columns.sql

-- profile_keyword 테이블에서 created_at 컬럼 삭제
ALTER TABLE profile_keyword
DROP COLUMN created_at;

-- conversation 테이블에서 intimacy_level 컬럼 삭제
ALTER TABLE conversation
DROP COLUMN intimacy_level;

-- conversation_card 테이블에서 swipe_type 컬럼 삭제
ALTER TABLE conversation_card
DROP COLUMN swipe_type;

