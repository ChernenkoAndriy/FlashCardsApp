SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

DELIMITER //

CREATE TABLE IF NOT EXISTS `user`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `username` varchar(50)  NOT NULL,
    `email`    varchar(100) NOT NULL,
    `password` varchar(255) NOT NULL,
    `language` varchar(30)  NOT NULL,
    `workload` int(11)   NOT NULL,
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
    `image`      varchar(2048) NULL,
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

CREATE TABLE IF NOT EXISTS `user_deck`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `deck_id` int(11) NOT NULL,
    `learned_number` int(11) NOT NULL DEFAULT 0,
    `progress` int (11) NOT NULL DEFAULT 0,
    `is_active`    bool                 NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`deck_id`) REFERENCES `deck` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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

    UPDATE user_deck
    SET progress = ROUND(learned_number * 100.0 / NULLIF(
            (SELECT cards_number FROM deck WHERE id = NEW.deck_id), 0))
    WHERE deck_id = NEW.deck_id;
END//

CREATE TRIGGER `decrement_deck_cards_number`
    AFTER DELETE
    ON `card`
    FOR EACH ROW
BEGIN
    UPDATE `deck`
    SET `cards_number` = `cards_number` - 1
    WHERE `id` = OLD.`deck_id`;

--     ТУТ МАЄ БУТИ ЩЕ ЗМІНА КІЛЬКОСТІ ВИВЧЕНИХ У ТИХ КОРИСТУВАЧІВ, ЯКІ ЦЮ КАРТКУ ВИВЧИЛИ, АЛЕ ВОНО НЕ ПРАЦЮЄ, ТОМУ Я ВРУЧНУ ЦЕ РОБЛЮ

    UPDATE user_deck
    SET progress = ROUND(learned_number * 100.0 / NULLIF(
            (SELECT cards_number FROM deck WHERE id = OLD.deck_id), 0))
    WHERE deck_id = OLD.deck_id;
END;
//


CREATE TRIGGER `after_insert_user_progress`
    AFTER INSERT ON `user_progress`
    FOR EACH ROW
BEGIN
    DECLARE deck INT;
    DECLARE total_number INT;

    SELECT deck_id INTO deck FROM card WHERE id = NEW.card_id;

    IF NOT EXISTS (
        SELECT 1 FROM user_deck
        WHERE user_id = NEW.user_id AND deck_id = deck
    ) THEN
        INSERT INTO user_deck (user_id, deck_id)
        VALUES (NEW.user_id, deck);
END IF;

IF NEW.is_learned = 1 THEN
UPDATE user_deck
SET learned_number = learned_number + 1
WHERE user_id = NEW.user_id AND deck_id = deck;
END IF;

SELECT cards_number INTO total_number
FROM deck
WHERE id = deck;

UPDATE user_deck
SET progress = ROUND(learned_number * 100.0 / NULLIF(total_number, 0))
WHERE user_id = NEW.user_id AND deck_id = deck;
END;
//

DELIMITER ;


-- ТЕСТОВІ ДАНІ --
INSERT INTO `user` (`username`, `email`, `password`, `language`, `workload`)
VALUES ('admin', 'admin@gmail.com', 'admin777', 'Ukrainian', 20),
       ('diana', 'diana@gmail.com', 'diana777', 'Ukrainian', 10),
       ('iryna', 'iryna@gmail.com', 'iryna777', 'German', 15),
       ('andriy', 'andriy@gmail.com', 'andriy777', 'English', 30);


INSERT INTO `language` (`name`)
VALUES ('English'), ('Ukrainian'), ('German');

-- English decks (language_id = 1)
INSERT INTO `deck` (`name`, `language_id`) VALUES

-- user`s decks
                                                            ('Simple words', 1),
                                                            ('Normal words', 1),
                                                            ('WORDS', 1),

-- Ukrainian decks (language_id = 2)
                                                            ('Я вчу українську', 2),

-- German decks (language_id = 3)
                                                            ('Deutsch', 3),
                                                            ('Deutsch 2', 3),

-- diana`s decks
                                                           ('Діана вчить англійську', 1),
                                                           ('Діана це вивчила', 1),
                                                           ('Діана вчить німецька', 3),
-- iryna`s decks
                                                           ('Irina lernt Englisch', 1),
                                                           ('Irina lernt Ukrainisch', 2);



-- User`s decks
INSERT INTO `user_deck` (`user_id`, `deck_id`) VALUES
                                               (1, 1),
                                               (1, 2),
                                               (1, 3),

-- Ukrainian decks (language_id = 2)
                                               (1, 4),

-- German decks (language_id = 3)
                                               (1, 5),
                                               (1, 6),

-- Diana`s decks
                                               (2, 7),
                                               (2, 8),
                                               (2, 9),
-- Iryna`s decks
                                               (3, 10),
                                               (3, 11);


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
                                                                               ('orange', 'апельсин', 'a citrus fruit', NULL, 3),

-- Cards for Ukrainian Deck 1 (deck_id = 4)
                                                                               ('вікно', 'window', 'an opening in a wall', NULL, 4),
                                                                               ('город', 'garden', 'a place to grow plants', NULL, 4),
                                                                               ('дерево', 'tree', 'a tall plant', NULL, 4),
                                                                               ('екран', 'screen', 'a surface to display info', NULL, 4),
                                                                               ('жук', 'beetle', 'a type of insect', NULL, 4),

-- Cards for German Deck 1 (deck_id = 5)
                                                                               ('Haus', 'будинок', 'a building where people live', NULL, 5),
                                                                               ('Stuhl', 'стілець', 'a seat with legs', NULL, 5),
                                                                               ('Tisch', 'стіл', 'a flat surface on legs', NULL, 5),

-- Cards for German Deck 2 (deck_id = 6)
                                                                               ('Fenster', 'вікно', 'an opening in a wall', NULL, 6),
                                                                               ('Tür', 'двері', 'a movable barrier', NULL, 6),
                                                                               ('Brot', 'хліб', 'baked food made from flour', NULL, 6),
                                                                               ('Milch', 'молоко', 'a drink from cows', NULL, 6),
                                                                               ('Wasser', 'вода', 'a liquid essential for life', NULL, 6),


-- Cards for Diana`s Deck 1 (deck_id = 7)
                                                                               ('hello', 'привіт', 'a greeting', NULL, 7),
                                                                               ('goodbye', 'до побачення', 'a farewell', NULL, 7),
                                                                               ('please', 'будь ласка', 'a polite request', NULL, 7),
                                                                               ('thank you', 'дякую', 'an expression of gratitude', NULL, 7),
                                                                               ('sorry', 'вибачте', 'an apology', NULL, 7),
-- Cards for Diana`s Deck 2 (deck_id = 8)
                                                                               ('yes', 'так', 'an affirmative response', NULL, 8),
                                                                               ('no', 'ні', 'a negative response', NULL, 8),
                                                                               ('maybe', 'можливо', 'an uncertain response', NULL, 8),
                                                                               ('help', 'допомога', 'assistance', NULL, 8),
                                                                               ('stop', 'стоп', 'to cease movement or action', NULL, 8),
-- Cards for Diana`s Deck 3 (deck_id = 9)
                                                         ('Katze', 'кіт', 'a domestic animal that purrs', NULL, 9),
                                                         ('Hund', 'пес', 'a loyal domestic animal', NULL, 9),
                                                         ('Vogel', 'птах', 'an animal that can fly', NULL, 9),
                                                         ('Fisch', 'риба', 'an animal that lives in water', NULL, 9),
                                                         ('Maus', 'миша', 'a small rodent', NULL, 9),


-- Cards for Iryna`s Deck 3 (deck_id = 10)
                                                                               ('water', 'вода', 'a liquid essential for life', NULL, 10),
                                                                               ('food', 'їжа', 'substance for nourishment', NULL, 10),
                                                                               ('sleep', 'сон', 'a state of rest', NULL, 10),
                                                                               ('play', 'грати', 'to engage in activity for enjoyment', NULL, 10),
                                                                               ('work', 'робота', 'activity involving mental or physical effort', NULL, 10),

-- Cards for Iryna`s Deck 2 (deck_id = 11)
                                                         ('заєць', 'hare', 'a fast-running animal with long ears', NULL, 11),
                                                         ('кіт', 'whale', 'a large marine mammal', NULL, 11),
                                                         ('лев', 'lion', 'a large wild cat known as the king of the jungle', NULL, 11),
                                                         ('панда', 'panda', 'a bear-like animal native to China', NULL, 11),
                                                         ('слон', 'elephant', 'the largest land animal with a trunk', NULL, 11);


INSERT INTO `user_progress` (`user_id`, `card_id`, `period`, `is_correct`, `next_date`, `is_learned`)
VALUES (1, 1, 'created', 0, NULL, 1),
       (1, 2, 'created', 0, NULL, 1),
       (1, 3, 'created', 0, NULL, 1),
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
       (1, 28, 'created', 0, NULL, 1),
       (1, 29, 'created', 0, NULL, 1),


-- Diana`s progress
               (2, 30, 'created', 0, NULL, 0),
               (2, 31, 'learning', 1, '2025-06-06 10:00:00', 0),
               (2, 32, 'first', 1, '2025-06-06 10:00:00', 0),
               (2, 33, 'second', 1, '2025-06-06 10:00:00', 0),
               (2, 34, 'third', 1, NULL, 1),
               (2, 35, 'third', 1, NULL, 1),
               (2, 36, 'third', 1, NULL, 1),
               (2, 37, 'third', 1, NULL, 1),
               (2, 38, 'third', 1, NULL, 1),
               (2, 39, 'third', 1, NULL, 1),
               (2, 40, 'second', 1, '2025-06-07 10:00:00', 0),
               (2, 41,  'second', 0, '2025-06-07 10:00:00', 0),
               (2, 42,  'second', 0, '2025-06-07 10:00:00', 0),
               (2, 43,  'second', 0, '2025-06-07 10:00:00', 0),
               (2, 44,  'second', 1, '2025-06-07 10:00:00', 0),


-- Iryna`s progress
               (3, 45, 'created', 0, NULL, 0),
               (3, 46, 'learning', 1, '2025-06-06 10:00:00', 0),
               (3, 47, 'first', 1, '2025-06-06 10:00:00', 0),
               (3, 48, 'second', 1, '2025-06-06 10:00:00', 0),
               (3, 49, 'third', 1, NULL, 1),
               (3, 50, 'third', 1, NULL, 1),
               (3, 51, 'third', 1, NULL, 1),
               (3, 52, 'third', 1, NULL, 1),
               (3, 53, 'third', 1, NULL, 1),
               (3, 54, 'third', 1, NULL, 1);