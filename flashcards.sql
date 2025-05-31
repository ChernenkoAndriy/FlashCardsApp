SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

DELIMITER //

CREATE TABLE IF NOT EXISTS `user`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `username` varchar(50)  NOT NULL,
    `email`    varchar(100) NOT NULL,
    `password` varchar(255) NOT NULL,
    `language` varchar(30)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

INSERT INTO `user` (`username`, `email`, `password`, `language`)
VALUES ('admin', 'admin@gmail.com', 'admin777', 'Ukrainian');

CREATE TABLE IF NOT EXISTS `language`
(
    `id`   int(11)     NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

INSERT INTO `language` (`name`)
VALUES ('English');

CREATE TABLE IF NOT EXISTS `deck`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT,
    `name`         varchar(50) NOT NULL,
    `cards_number` int(11)     NOT NULL DEFAULT 0,
    `is_active`    bool        NOT NULL DEFAULT 1,
    `language_id`  int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

INSERT INTO `deck` (`name`, `language_id`, `is_active`)
VALUES ('TEST', 1, 1);

CREATE TABLE IF NOT EXISTS `card`
(
    `id`         int(11)     NOT NULL AUTO_INCREMENT,
    `word`       varchar(50) NOT NULL,
    `translate`  varchar(50) NOT NULL,
    `definition` varchar(100)         DEFAULT NOT NULL,
    `image`      varchar(255)         DEFAULT NULL,
    `is_learned` bool        NOT NULL DEFAULT 0,
    `deck_id`    int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`deck_id`) REFERENCES `deck` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

INSERT INTO `card` (`word`, `translate`, `definition`, `image`, `is_learned`, `deck_id`)
VALUES ('test', 'тест', 'a procedure intended to establish the quality, performance, or reliability of something', NULL,
        0, 1);

CREATE TABLE IF NOT EXISTS `user_progress`
(
    `id`         int(11)                                       NOT NULL AUTO_INCREMENT,
    `user_id`    int(11)                                       NOT NULL,
    `card_id`    int(11)                                       NOT NULL,
    `period`     enum ('learning', 'first', 'second', 'third') NOT NULL DEFAULT 'learning',
    `is_correct` bool                                          NOT NULL DEFAULT 0,
    `date`       datetime                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`card_id`) REFERENCES `card` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

INSERT INTO `user_progress` (`user_id`, `card_id`, `period`, `is_correct`)
VALUES (1, 1, 'learning', 0);

CREATE TRIGGER `update_deck_cards_number`
    AFTER INSERT
    ON `card`
    FOR EACH ROW
BEGIN
    UPDATE `deck`
    SET `cards_number` = `cards_number` + 1
    WHERE `id` = NEW.`deck_id`;
END//

CREATE TRIGGER `decrement_deck_cards_number`
    AFTER DELETE
    ON `card`
    FOR EACH ROW
BEGIN
    UPDATE `deck`
    SET `cards_number` = `cards_number` - 1
    WHERE `id` = OLD.`deck_id`;
END//


DELIMITER ;