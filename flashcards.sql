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
    `workload` int(11)      NOT NULL,
    `role`     ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `password_reset_token`
(
    `id`         int(11)      NOT NULL AUTO_INCREMENT,
    `user_id`    int(11)      NOT NULL,
    `token`      varchar(255) NOT NULL,
    `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `expires_at` datetime     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY `token` (`token`)
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

    UPDATE user_deck
    SET progress = ROUND(learned_number * 100.0 / NULLIF(
            (SELECT cards_number FROM deck WHERE id = OLD.deck_id), 0))
    WHERE deck_id = OLD.deck_id;
END//

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
END//

DELIMITER ;

-- TEST DATA --
INSERT INTO `user` (`username`, `email`, `password`, `language`, `workload`, `role`)
VALUES
    ('admin', 'admin@gmail.com',  '$2b$12$aZ4h1zpt2IMYHOzX6pai2.KjFLYds6XwvHhB27ywM5BzaPdBov69e', 'Ukrainian', 20, 'ADMIN'),
    ('diana', 'diana@gmail.com',  '$2b$12$JflBY7U.BFN09ohAcbv.P.7xFkoHyareKdyYWUpMKd.0A5L4mdCCK', 'Ukrainian', 10, 'USER'),
    ('iryna', 'iryna@gmail.com',  '$2b$12$6Ep8laW8QNXbclPLxBxhb.RbPTn79TRO2j6KceT3Ap8mWPdrtj6AW', 'German',    15, 'USER'),
    ('andriy','andriy@gmail.com', '$2b$12$sqk5K0REYvXVwoI8QzZd7eJrW7hdwhCV2IHVvg1aDyzStprC477lS', 'English',   30, 'USER');

INSERT INTO `language` (`name`)
VALUES ('English'), ('Ukrainian'), ('German');

-- English decks (language_id = 1)
INSERT INTO `deck` (`name`, `language_id`) VALUES
                                               ('Simple words', 1),
                                               (' Decent words', 1),
                                               ('WORDS', 1),
                                               ('Я вчу українську', 2),
                                               ('Deutsch', 3),
                                               ('Deutsch 2', 3),
                                               ('Діана вчить англійську', 1),
                                               ('Діана це вив

чила', 1),
                                               ('Діана вчить німецька', 3),
                                               ('Irina lernt Englisch', 1),
                                               ('Irina lernt Ukrainisch', 2);

-- User`s decks
INSERT INTO `user_deck` (`user_id`, `deck_id`) VALUES
                                                   (1, 1),
                                                   (1, 2),
                                                   (1, 3),
                                                   (1, 4),
                                                   (1, 5),
                                                   (1, 6),
                                                   (2, 7),
                                                   (2, 8),
                                                   (2, 9),
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
                                                                               ('fish', 'риба', 'an animal that swims', NULL, 2),
                                                                               ('glass', 'склянка', 'a container for drinking', NULL, 2),
                                                                               ('hat', 'капелюх', 'a thing you wear on your head', NULL, 2),
                                                                               ('ice', 'лід', 'frozen water', NULL, 2),
                                                                               ('juice', 'сік', 'a drink made from fruit', NULL, 2),
                                                                               ('kite', 'повітряний змій', 'a toy that flies in the wind', NULL, 3),
                                                                               ('lamp', 'лампа', 'a device for giving light', NULL, 3),
                                                                               ('moon', 'місяць', 'a satellite of the Earth', NULL, 3),
                                                                               ('nest', 'гніздо', 'a birds home', NULL, 3),
                                                                               ('orange', 'апельсин', 'a citrus fruit', NULL, 3),
                                                                               ('вікно', 'window', 'an opening in a wall', NULL, 4),
                                                                               ('город', 'garden', 'a place to grow plants', NULL, 4),
                                                                               ('дерево', 'tree', 'a tall plant', NULL, 4),
                                                                               ('екран', 'screen', 'a surface to display info', NULL, 4),
                                                                               ('жук', 'beetle', 'a type of insect', NULL, 4),
                                                                               ('Haus', 'будинок', 'a building where people live', NULL, 5),
                                                                               ('Stuhl', 'стілець', 'a seat with legs', NULL, 5),
                                                                               ('Tisch', 'стіл', 'a flat surface on legs', NULL, 5),
                                                                               ('Fenster', 'вікно', 'an opening in a wall', NULL, 6),
                                                                               ('Tür', 'двері', 'a movable barrier', NULL, 6),
                                                                               ('Brot', 'хліб', 'baked food made from flour', NULL, 6),
                                                                               ('Milch', 'молоко', 'a drink from cows', NULL, 6),
                                                                               ('Wasser', 'вода', 'a liquid essential for life', NULL, 6),
                                                                               ('hello', 'привіт', 'a greeting', NULL, 7),
                                                                               ('goodbye', 'до побачення', 'a farewell', NULL, 7),
                                                                               ('please', 'будь ласка', 'a polite request', NULL, 7),
                                                                               ('thank you', 'дякую', 'an expression of gratitude', NULL, 7),
                                                                               ('sorry', 'вибачте', 'an apology', NULL, 7),
                                                                               ('yes', 'так', 'an affirmative response', NULL, 8),
                                                                               ('no', 'ні', 'a negative response', NULL, 8),
                                                                               ('maybe', 'можливо', 'an uncertain response', NULL, 8),
                                                                               ('help', 'допомога', 'assistance', NULL, 8),
                                                                               ('stop', 'стоп', 'to cease movement or action', NULL, 8),
                                                                               ('Katze', 'кіт', 'a domestic animal that purrs', NULL, 9),
                                                                               ('Hund', 'пес', 'a loyal domestic animal', NULL, 9),
                                                                               ('Vogel', 'птах', 'an animal that can fly', NULL, 9),
                                                                               ('Fisch', 'риба', 'an animal that lives in water', NULL, 9),
                                                                               ('Maus', 'миша', 'a small rodent', NULL, 9),
                                                                               ('water', 'вода', 'a liquid essential for life', NULL, 10),
                                                                               ('food', 'їжа', 'substance for nourishment', NULL, 10),
                                                                               ('sleep', 'сон', 'a state of rest', NULL, 10),
                                                                               ('play', 'грати', 'to engage in activity for enjoyment', NULL, 10),
                                                                               ('work', 'робота', 'activity involving mental or physical effort', NULL, 10),
                                                                               ('заєць', 'hare', 'a fast-running animal with long ears', NULL, 11),
                                                                               ('кіт', 'whale', 'a large marine mammal', NULL, 11),
                                                                               ('лев', 'lion', 'a large wild cat known as the king of the jungle', NULL, 11),
                                                                               ('панда', 'panda', 'a bear-like animal native to China', NULL, 11),
                                                                               ('слон', 'elephant', 'the largest land animal with a trunk', NULL, 11),

('friend', 'друг', 'a person whom one knows and with whom one has a bond of mutual affection', NULL, 7),
       ('family', 'родина', 'a group consisting of parents and children living together in a household', NULL, 7),
       ('house', 'будинок', 'a building for human habitation', NULL, 7),
       ('school', 'школа', 'an institution for educating children', NULL, 7),
       ('teacher', 'вчитель', 'a person who teaches', NULL, 7),
       ('student', 'студент', 'a person who is studying at a school or college', NULL, 7),
       ('city', 'місто', 'a large town', NULL, 7),
       ('country', 'країна', 'a nation or state', NULL, 7),
       ('travel', 'подорожувати', 'make a journey, typically of some length', NULL, 7),
       ('learn', 'вчити', 'gain or acquire knowledge of or skill in', NULL, 7),
 ('Apfel', 'яблуко', 'eine runde Frucht mit süßem Geschmack', NULL, 9),
       ('Buch', 'книга', 'ein Werk mit geschriebenen oder gedruckten Seiten', NULL, 9),
       ('Wasser', 'вода', 'eine farblose, durchsichtige Flüssigkeit, die zum Leben notwendig ist', NULL, 9),
       ('Essen', 'їжа', 'Substanz, die zur Ernährung dient', NULL, 9),
       ('trinken', 'пити', 'Flüssigkeit zu sich nehmen', NULL, 9),
       ('gehen', 'йти', 'sich zu Fuß fortbewegen', NULL, 9),
       ('lesen', 'читати', 'geschriebene oder gedruckte Texte verstehen', NULL, 9),
       ('sprechen', 'говорити', 'Wörter und Sätze äußern', NULL, 9),
       ('schreiben', 'писати', 'Buchstaben oder Zeichen auf einer Oberfläche anbringen', NULL, 9),
       ('Tag', 'день', 'die Zeitspanne von Sonnenaufgang bis Sonnenuntergang', NULL, 9);


-- User Progress for Diana (user_id = 2) for the newly added cards
INSERT INTO `user_progress` (`user_id`, `card_id`, `period`, `is_correct`, `next_date`, `is_learned`)
VALUES (2, 55, 'created', 0, NULL, 0), -- friend (deck 7)
       (2, 56, 'created', 0, NULL, 0), -- family (deck 7)
       (2, 57, 'created', 0, NULL, 0), -- house (deck 7)
       (2, 58, 'created', 0, NULL, 0), -- school (deck 7)
       (2, 59, 'created', 0, NULL, 0), -- teacher (deck 7)
       (2, 60, 'created', 0, NULL, 0), -- student (deck 7)
       (2, 61, 'created', 0, NULL, 0), -- city (deck 7)
       (2, 62, 'created', 0, NULL, 0), -- country (deck 7)
       (2, 63, 'created', 0, NULL, 0), -- travel (deck 7)
       (2, 64, 'created', 0, NULL, 0), -- learn (deck 7)
       (2, 65, 'created', 0, NULL, 0), -- Apfel (deck 9)
       (2, 66, 'created', 0, NULL, 0), -- Buch (deck 9)
       (2, 67, 'created', 0, NULL, 0), -- Wasser (deck 9)
       (2, 68, 'created', 0, NULL, 0), -- Essen (deck 9)
       (2, 69, 'created', 0, NULL, 0), -- trinken (deck 9)
       (2, 70, 'created', 0, NULL, 0), -- gehen (deck 9)
       (2, 71, 'created', 0, NULL, 0), -- lesen (deck 9)
       (2, 72, 'created', 0, NULL, 0), -- sprechen (deck 9)
       (2, 73, 'created', 0, NULL, 0), -- schreiben (deck 9)
       (2, 74, 'created', 0, NULL, 0), -- Tag (deck 9)


    (1, 1, 'created', 0, NULL, 1),
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
    (1, 25, 'created', 0, NULL

    , 0),
    (1, 26, 'created', 0, NULL, 0),
    (1, 27, 'created', 0, NULL, 0),
    (1, 28, 'created', 0, NULL, 1),
    (1, 29, 'created', 0, NULL, 1),
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
    (2, 41, 'second', 0, '2025-06-07 10:00:00', 0),
    (2, 42, 'second', 0, '2025-06-07 10:00:00', 0),
    (2, 43, 'second', 0, '2025-06-07 10:00:00', 0),
    (2, 44, 'second', 1, '2025-06-07 10:00:00', 0),
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