-- V2__add_user_device_table.sql

CREATE TABLE user_device (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             fcm_token VARCHAR(255) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             last_login_at TIMESTAMP,
                             PRIMARY KEY (id),
                             UNIQUE KEY uk_fcm_token (fcm_token),
                             FOREIGN KEY (user_id) REFERENCES user(id)
);
