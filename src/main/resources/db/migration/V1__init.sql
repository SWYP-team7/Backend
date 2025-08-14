-- V1__init.sql
CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      email VARCHAR(255),
                      birthdate DATE NOT NULL,
                      gender VARCHAR(10) NOT NULL,
                      provider VARCHAR(50) NOT NULL,
                      provider_id VARCHAR(255) NOT NULL,
                      user_code VARCHAR(50) NOT NULL UNIQUE,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      deleted_at TIMESTAMP,
                      PRIMARY KEY (id),
                      UNIQUE KEY uk_user_provider (provider, provider_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE profile_keyword (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 content VARCHAR(512) NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 deleted_at TIMESTAMP,
                                 PRIMARY KEY (id),
                                 UNIQUE KEY uk_profile_keyword_content (content(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_profile_keyword (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      user_id BIGINT NOT NULL,
                                      profile_keyword_id BIGINT NOT NULL,
                                      PRIMARY KEY (id),
                                      UNIQUE KEY uk_user_profile_keyword (user_id, profile_keyword_id),
                                      FOREIGN KEY (user_id) REFERENCES user(id),
                                      FOREIGN KEY (profile_keyword_id) REFERENCES profile_keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE friend (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        friend_id BIGINT NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_friend_user_pair (user_id, friend_id),
                        CONSTRAINT ck_friend_not_self CHECK (user_id <> friend_id),
                        FOREIGN KEY (user_id) REFERENCES user(id),
                        FOREIGN KEY (friend_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              ended_at TIMESTAMP,
                              PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE participant (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             conversation_id BIGINT NOT NULL,
                             user_id BIGINT, -- 회원인 경우 user.id, 비회원인 경우 NULL
                             non_member_name VARCHAR(100), -- 비회원인 경우 사용할 이름 (예: "사용자1(비회원)")
                             relationship VARCHAR(50) NOT NULL,
                             intimacy_level INT,
                             PRIMARY KEY (id),
                             UNIQUE KEY uk_participant_conversation_user (conversation_id, user_id),
                             FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                             FOREIGN KEY (user_id) REFERENCES user(id),
                             CONSTRAINT chk_participant_type CHECK (user_id IS NOT NULL OR non_member_name IS NOT NULL)
);

CREATE TABLE category (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(1024) NOT NULL,
                          deleted_at TIMESTAMP,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_keyword (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      content VARCHAR(512) NOT NULL,
                                      is_predefined BOOLEAN NOT NULL DEFAULT FALSE,
                                      deleted_at TIMESTAMP,
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
                                        FOREIGN KEY (user_id) REFERENCES user(id),
                                        FOREIGN KEY (conversation_card_id) REFERENCES conversation_card(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_log (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  conversation_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  utterance_text TEXT,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                  FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_report (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     conversation_id BIGINT NOT NULL,
                                     duration_seconds INT,
                                     num_questions INT,
                                     num_hearts INT,
                                     summary TEXT,
                                     PRIMARY KEY (id),
                                     UNIQUE KEY uk_report_conversation (conversation_id),
                                     FOREIGN KEY (conversation_id) REFERENCES conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE conversation_report_keyword (
                                             conversation_report_id BIGINT NOT NULL,
                                             conversation_keyword_id BIGINT NOT NULL,
                                             PRIMARY KEY (conversation_report_id, conversation_keyword_id),
                                             FOREIGN KEY (conversation_report_id) REFERENCES conversation_report(id),
                                             FOREIGN KEY (conversation_keyword_id) REFERENCES conversation_keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
