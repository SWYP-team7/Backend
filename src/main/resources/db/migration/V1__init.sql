-- V1__init.sql

-- =======================================================================
-- 1. USERS & PROFILES
-- =======================================================================
CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(100),
                       email VARCHAR(255),
                       birthdate DATE,
                       gender VARCHAR(10),
                       profile_image_url VARCHAR(2048),
                       provider VARCHAR(50) NOT NULL,
                       provider_id VARCHAR(255) NOT NULL,
                       profile_completed BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_user_provider (provider, provider_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE profile_keyword_category (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          name VARCHAR(100) NOT NULL UNIQUE,
                                          display_order INT,
                                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE profile_keyword (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 category_id BIGINT NOT NULL,
                                 content VARCHAR(512) NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (id),
                                 UNIQUE KEY uk_profile_keyword_content (content(255)),
                                 FOREIGN KEY (category_id) REFERENCES profile_keyword_category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_profile_keyword (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      user_id BIGINT NOT NULL,
                                      profile_keyword_id BIGINT NOT NULL,
                                      PRIMARY KEY (id),
                                      UNIQUE KEY uk_user_profile_keyword (user_id, profile_keyword_id),
                                      FOREIGN KEY (user_id) REFERENCES users(id),
                                      FOREIGN KEY (profile_keyword_id) REFERENCES profile_keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================================
-- 2. CONVERSATION CORE
-- =======================================================================
CREATE TABLE conversation (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              creator_user_id BIGINT NOT NULL,
                              relationship VARCHAR(50),
                              intimacy_level INT,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              ended_at TIMESTAMP,
                              PRIMARY KEY (id),
                              FOREIGN KEY (creator_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE participant (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             conversation_id BIGINT NOT NULL,
                             name VARCHAR(100) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (conversation_id) REFERENCES conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================================
-- 3. CONVERSATION METADATA & CONTENT
-- =======================================================================
CREATE TABLE category (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(1024) NOT NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_keyword (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      content VARCHAR(512) NOT NULL,
                                      is_predefined BOOLEAN NOT NULL DEFAULT FALSE,
                                      PRIMARY KEY (id),
                                      UNIQUE KEY uk_conversation_keyword_content (content(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_category (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       conversation_id BIGINT NOT NULL,
                                       category_id BIGINT NOT NULL,
                                       PRIMARY KEY (id),
                                       UNIQUE KEY uk_convo_category (conversation_id, category_id),
                                       FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                       FOREIGN KEY (category_id) REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE selected_conversation_keyword (
                                               id BIGINT NOT NULL AUTO_INCREMENT,
                                               conversation_id BIGINT NOT NULL,
                                               conversation_keyword_id BIGINT NOT NULL,
                                               PRIMARY KEY (id),
                                               UNIQUE KEY uk_selected_convo_keyword (conversation_id, conversation_keyword_id),
                                               FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                               FOREIGN KEY (conversation_keyword_id) REFERENCES conversation_keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_card (
                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                   conversation_id BIGINT NOT NULL,
                                   content TEXT NOT NULL,
                                   level INT NOT NULL,
                                   order_index INT NOT NULL,
                                   swipe_type VARCHAR(20) NOT NULL,
                                   source VARCHAR(100) NOT NULL,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (conversation_id) REFERENCES conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_card_save (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        user_id BIGINT NOT NULL,
                                        conversation_card_id BIGINT NOT NULL,
                                        saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        PRIMARY KEY (id),
                                        UNIQUE KEY uk_card_save_user_card (user_id, conversation_card_id),
                                        FOREIGN KEY (user_id) REFERENCES users(id),
                                        FOREIGN KEY (conversation_card_id) REFERENCES conversation_card(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =======================================================================
-- 4. CONVERSATION LOG & REPORT
-- =======================================================================
CREATE TABLE conversation_log (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  conversation_id BIGINT NOT NULL,
                                  participant_name VARCHAR(100) NOT NULL,
                                  utterance_text TEXT,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (conversation_id) REFERENCES conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_report (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     conversation_id BIGINT NOT NULL,
                                     share_uuid VARCHAR(36) UNIQUE,
                                     duration_seconds INT,
                                     num_questions INT,
                                     num_hearts INT,
                                     summary TEXT,
                                     PRIMARY KEY (id),
                                     UNIQUE KEY uk_report_conversation (conversation_id),
                                     FOREIGN KEY (conversation_id) REFERENCES conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_report_keyword (
                                             id BIGINT NOT NULL AUTO_INCREMENT,
                                             conversation_report_id BIGINT NOT NULL,
                                             conversation_keyword_id BIGINT NOT NULL,
                                             PRIMARY KEY (id),
                                             UNIQUE KEY uk_report_keyword_pair (conversation_report_id, conversation_keyword_id),
                                             FOREIGN KEY (conversation_report_id) REFERENCES conversation_report(id),
                                             FOREIGN KEY (conversation_keyword_id) REFERENCES conversation_keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
