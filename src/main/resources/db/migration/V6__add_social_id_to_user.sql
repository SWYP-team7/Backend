-- V6__add_social_id_to_user.sql

-- user 테이블에 social id 컬럼 추가
ALTER TABLE users
ADD COLUMN social_id BIGINT UNIQUE;
