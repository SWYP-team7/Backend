-- V14__add_card_keyword_column_to_conversation_card_save.sql

ALTER TABLE conversation_card
    ADD COLUMN card_keyword VARCHAR(100) NOT NULL;
