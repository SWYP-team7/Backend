-- USER
CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      email VARCHAR(255),
                      birthdate DATE NOT NULL,
                      provider VARCHAR(50) NOT NULL,
                      provider_id VARCHAR(255) NOT NULL,
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP NOT NULL,
                      deleted_at TIMESTAMP,
                      UNIQUE (provider, provider_id),
                      PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- USER KEYWORD
CREATE TABLE user_keyword (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              keyword VARCHAR(1024) NOT NULL,
                              created_at TIMESTAMP NOT NULL,
                              PRIMARY KEY (id),
                              FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- FRIEND
CREATE TABLE friend (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        friend_id BIGINT NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        deleted_at TIMESTAMP,
                        active TINYINT AS (CASE WHEN deleted_at IS NULL THEN 1 ELSE 0 END) STORED,
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_friend_active (user_id, friend_id, active),
                        CONSTRAINT ck_friend_not_self CHECK (user_id <> friend_id),
                        FOREIGN KEY (user_id) REFERENCES user(id),
                        FOREIGN KEY (friend_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CONVERSATION
CREATE TABLE conversation (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              created_at TIMESTAMP NOT NULL,
                              ended_at TIMESTAMP,
                              relationship VARCHAR(50) NOT NULL,
                              intimacy_level INT,
                              deleted_at TIMESTAMP,
                              PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- PARTICIPANT
CREATE TABLE participant (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             conversation_id BIGINT NOT NULL,
                             user_id BIGINT NOT NULL,
                             created_at TIMESTAMP NOT NULL,
                             PRIMARY KEY (id),
                             UNIQUE (conversation_id, user_id),
                             FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                             FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CONVERSATION CARD
CREATE TABLE conversation_card (
                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                   conversation_id BIGINT NOT NULL,
                                   content TEXT NOT NULL,
                                   level INT NOT NULL,
                                   order_index INT NOT NULL,
                                   swipe_type VARCHAR(20) NOT NULL,
                                   source VARCHAR(100) NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   deleted_at TIMESTAMP,
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                   INDEX idx_card_convo_order (conversation_id, deleted_at, order_index)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CARD SAVE
CREATE TABLE conversation_card_save (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        user_id BIGINT NOT NULL,
                                        conversation_card_id BIGINT NOT NULL,
                                        saved_at TIMESTAMP NOT NULL,
                                        deleted_at TIMESTAMP,
                                        PRIMARY KEY (id),
                                        UNIQUE (user_id, conversation_card_id, deleted_at),
                                        FOREIGN KEY (user_id) REFERENCES user(id),
                                        FOREIGN KEY (conversation_card_id) REFERENCES conversation_card(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CONVERSATION LOG
CREATE TABLE conversation_log (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  conversation_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  utterance_text TEXT,
                                  created_at TIMESTAMP NOT NULL,
                                  deleted_at TIMESTAMP,
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                  FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CATEGORY
CREATE TABLE category (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(1024) NOT NULL,
                          deleted_at TIMESTAMP,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10) CONVERSATION CATEGORY
CREATE TABLE conversation_category (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       conversation_id BIGINT NOT NULL,
                                       category_id BIGINT NOT NULL,
                                       created_at TIMESTAMP NOT NULL,
                                       PRIMARY KEY (id),
                                       UNIQUE (conversation_id, category_id),
                                       FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                       FOREIGN KEY (category_id) REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- KEYWORD
CREATE TABLE keyword (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         deleted_at TIMESTAMP,
                         PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CONVERSATION KEYWORD
CREATE TABLE conversation_keyword (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      conversation_id BIGINT NOT NULL,
                                      keyword_id BIGINT NOT NULL,
                                      created_at TIMESTAMP NOT NULL,
                                      PRIMARY KEY (id),
                                      UNIQUE (conversation_id, keyword_id),
                                      FOREIGN KEY (conversation_id) REFERENCES conversation(id),
                                      FOREIGN KEY (keyword_id) REFERENCES keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CONVERSATION REPORT
CREATE TABLE conversation_report (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     conversation_id BIGINT NOT NULL,
                                     duration_seconds INT,
                                     num_questions INT,
                                     num_hearts INT,
                                     summary TEXT,
                                     created_at TIMESTAMP NOT NULL,
                                     deleted_at TIMESTAMP,
                                     PRIMARY KEY (id),
                                     UNIQUE (conversation_id),
                                     FOREIGN KEY (conversation_id) REFERENCES conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- REPORT KEYWORD
CREATE TABLE conversation_report_keyword (
                                             conversation_report_id BIGINT NOT NULL,
                                             keyword_id BIGINT NOT NULL,
                                             PRIMARY KEY (conversation_report_id, keyword_id),
                                             FOREIGN KEY (conversation_report_id) REFERENCES conversation_report(id),
                                             FOREIGN KEY (keyword_id) REFERENCES keyword(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
