-- V3__flatten_profile_keyword_category.sql

-- profile_keyword 테이블에 category_name 컬럼 추가
ALTER TABLE profile_keyword
    ADD COLUMN category_name VARCHAR(100);

-- 기존 category_id를 이용해 category_name 값 채워넣기
UPDATE profile_keyword pk
    JOIN profile_keyword_category pkc ON pk.category_id = pkc.id
    SET pk.category_name = pkc.name;

-- 외래키 제약조건 제거
ALTER TABLE profile_keyword
DROP FOREIGN KEY profile_keyword_ibfk_1;

-- category_id 컬럼 제거
ALTER TABLE profile_keyword
DROP COLUMN category_id;

-- profile_keyword_category 테이블 제거
DROP TABLE profile_keyword_category;
