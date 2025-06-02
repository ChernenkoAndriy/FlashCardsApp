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

CREATE TABLE IF NOT EXISTS `language`
(
    `id`   int(11)     NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `deck`
(
    `id`           int(11)              NOT NULL AUTO_INCREMENT,
    `name`         varchar(50)          NOT NULL,
    `cards_number` int(11)              NOT NULL DEFAULT 0,
    `is_active`    bool                 NOT NULL DEFAULT 1,
    `language_id`  int(11)              NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `card`
(
    `id`         int(11)      NOT NULL AUTO_INCREMENT,
    `word`       varchar(50)  NOT NULL,
    `translate`  varchar(50)  NOT NULL,
    `definition` varchar(100) NOT NULL,
    `image`      varchar(255) NULL,
    `deck_id`    int(11)      NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`deck_id`) REFERENCES `deck` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `user_progress`
(
    `id`         int(11)                                                    NOT NULL AUTO_INCREMENT,
    `user_id`    int(11)                                                    NOT NULL,
    `card_id`    int(11)                                                    NOT NULL,
    `period`     enum ('created','learning', 'first', 'second', 'third')    NOT NULL DEFAULT 'created',
    `is_correct` bool                                                       NOT NULL DEFAULT 0,
    `date`       datetime                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `next_date`  datetime                                                   NULL,
    `is_learned` bool                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`card_id`) REFERENCES `card` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

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


-- ТЕСТОВІ ДАНІ --
INSERT INTO `user` (`username`, `email`, `password`, `language`)
VALUES ('admin', 'admin@gmail.com', 'admin777', 'Ukrainian');

INSERT INTO `language` (`name`)
VALUES ('English'), ('Ukrainian'), ('German');

-- English decks (language_id = 1)
INSERT INTO `deck` (`name`, `language_id`, `is_active`) VALUES
                                                            ('Simple words', 1, 1),
                                                            ('Normal words', 1, 1),
                                                            ('WORDS', 1, 1),

-- Ukrainian decks (language_id = 2)
                                                            ('Я вчу українську', 2, 1),

-- German decks (language_id = 3)
                                                            ('Deutsch', 3, 1),
                                                            ('Deutsch 2', 3, 1);



-- Cards for English Deck 1 (deck_id = 1)
INSERT INTO `card` (`word`, `translate`, `definition`, `image`, `deck_id`) VALUES
                                                                               ('test', 'тест', 'a procedure intended to establish the quality, performance, or reliability of something', NULL, 1),
                                                                               ('apple', 'яблуко', 'a fruit', NULL, 1),
                                                                               ('book', 'книга', 'something you read', NULL, 1),
                                                                               ('car', 'автомобіль', 'a vehicle', NULL, 1),
                                                                               ('dog', 'пес', 'a domestic animal', NULL, 1),
                                                                               ('egg', 'яйце', 'food from chicken', NULL, 1),
-- Cards for English Deck 2 (deck_id = 2)
                                                                               ('fish', 'риба', 'an animal that swims', NULL, 2),
                                                                               ('glass', 'склянка', 'a container for drinking', NULL, 2),
                                                                               ('hat', 'капелюх', 'a thing you wear on your head', NULL, 2),
                                                                               ('ice', 'лід', 'frozen water', NULL, 2),
                                                                               ('juice', 'сік', 'a drink made from fruit', NULL, 2),

-- Cards for English Deck 3 (deck_id = 3)
                                                                               ('kite', 'повітряний змій', 'a toy that flies in the wind', NULL, 3),
                                                                               ('lamp', 'лампа', 'a device for giving light', NULL, 3),
                                                                               ('moon', 'місяць', 'a satellite of the Earth', NULL, 3),
                                                                               ('nest', 'гніздо', 'a birds home', NULL, 3),
                                                                               ('orange', 'апельсин', 'a citrus fruit', NULL, 3);

-- Cards for Ukrainian Deck 1 (deck_id = 4)
INSERT INTO `card` (`word`, `translate`, `definition`, `image`, `deck_id`) VALUES
                                                                               ('вікно', 'window', 'an opening in a wall', NULL, 4),
                                                                               ('город', 'garden', 'a place to grow plants', NULL, 4),
                                                                               ('дерево', 'tree', 'a tall plant', NULL, 4),
                                                                               ('екран', 'screen', 'a surface to display info', NULL, 4),
                                                                               ('жук', 'beetle', 'a type of insect', NULL, 4);

-- Cards for German Deck 1 (deck_id = 5)
INSERT INTO `card` (`word`, `translate`, `definition`, `image`, `deck_id`) VALUES
                                                                               ('Haus', 'будинок', 'a building where people live', NULL, 5),
                                                                               ('Stuhl', 'стілець', 'a seat with legs', NULL, 5),
                                                                               ('Tisch', 'стіл', 'a flat surface on legs', NULL, 5);

-- Cards for German Deck 2 (deck_id = 6)
INSERT INTO `card` (`word`, `translate`, `definition`, `image`, `deck_id`) VALUES
                                                                               ('Fenster', 'вікно', 'an opening in a wall', NULL, 6),
                                                                               ('Tür', 'двері', 'a movable barrier', NULL, 6),
                                                                               ('Brot', 'хліб', 'baked food made from flour', NULL, 6),
                                                                               ('Milch', 'молоко', 'a drink from cows', NULL, 6),
                                                                               ('Wasser', 'вода', 'a liquid essential for life', NULL, 6);


INSERT INTO `user_progress` (`user_id`, `card_id`, `period`, `is_correct`, `next_date`, `is_learned`)
VALUES (1, 1, 'created', 0, NULL, 0),
       (1, 2, 'created', 0, NULL, 0),
       (1, 3, 'created', 0, NULL, 0),
       (1, 4, 'created', 0, NULL, 0),
       (1, 5, 'created', 0, NULL, 0),
       (1, 6, 'created', 0, NULL, 0),
       (1, 7, 'created', 0, NULL, 0),
       (1, 8, 'created', 0, NULL, 0),
       (1, 9, 'created', 0, NULL, 0),
       (1, 10, 'created', 0, NULL, 0),
       (1, 11, 'created', 0, NULL, 0),
       (1, 12, 'created', 0, NULL, 0),
       (1, 13, 'created', 0, NULL, 0),
       (1, 14, 'created', 0, NULL, 0),
       (1, 15, 'created', 0, NULL, 0),
       (1, 16, 'created', 0, NULL, 0),
       (1, 17, 'created', 0, NULL, 0),
       (1, 18, 'created', 0, NULL, 0),
       (1, 19, 'created', 0, NULL, 0),
       (1, 20, 'created', 0, NULL, 0),
       (1, 21, 'created', 0, NULL, 0),
       (1, 22, 'created', 0, NULL, 0),
       (1, 23, 'created', 0, NULL, 0),
       (1, 24, 'created', 0, NULL, 0),
       (1, 25, 'created', 0, NULL, 0),
       (1, 26, 'created', 0, NULL, 0),
       (1, 27, 'created', 0, NULL, 0),
       (1, 28, 'created', 0, NULL, 0),
       (1, 29, 'created', 0, NULL, 0);