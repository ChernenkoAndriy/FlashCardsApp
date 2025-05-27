SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";



CREATE TABLE IF NOT EXISTS `user`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `username` varchar(50)  NOT NULL,
    `email`    varchar(100) NOT NULL,
    `password` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


INSERT INTO `user` (`username`, `email`, `password`)
VALUES ('admin', 'admin@gmail.com', 'admin777');


CREATE TABLE IF NOT EXISTS `deck`
(
    `id`            int(11)     NOT NULL AUTO_INCREMENT,
    `name`          varchar(50) NOT NULL,
    `language_from` varchar(30) NOT NULL,
    `language_to`   varchar(30) NOT NULL,
    `workload`      int(11)     NOT NULL DEFAULT 0,
    `user_id`       int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


INSERT INTO `deck` (`name`, `language_from`, `language_to`, `workload`, `user_id`)
VALUES ('TEST', 'Ukrainian', 'English', 10, 1);

CREATE TABLE IF NOT EXISTS `card`
(
    `id`         int(11)     NOT NULL AUTO_INCREMENT,
    `word`       varchar(50) NOT NULL,
    `translate`  varchar(50) NOT NULL,
    `definition` varchar(100)         DEFAULT NULL,
    `image`      varchar(255)         DEFAULT NULL,
    `is_learned` bool        NOT NULL DEFAULT 0,
    `deck_id`    int(11)     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`deck_id`) REFERENCES `deck` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

INSERT INTO `card` (`word`, `translate`, `definition`, `image`, `is_learned`, `deck_id`)
VALUES ('test', 'тест', 'a procedure intended to establish the quality, performance, or reliability of something', NULL, 0, 1);

