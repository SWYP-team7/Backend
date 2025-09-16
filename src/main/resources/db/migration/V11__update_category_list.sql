DELETE FROM category
WHERE display_order IN (4, 5, 6);

-- 2. 새 category 추가
INSERT INTO category (content, display_order)
VALUES ('가치관 & 철학', 4),
       ('도파민', 5);
