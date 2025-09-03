-- conversation_report 테이블 수정
ALTER TABLE conversation_report
    CHANGE COLUMN summary comment TEXT NOT NULL,
    ADD COLUMN next_recommended_topic VARCHAR(100) NOT NULL AFTER comment;

-- 필요 없어진 테이블 삭제
DROP TABLE IF EXISTS conversation_report_keyword;
