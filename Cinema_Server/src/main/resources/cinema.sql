-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
    'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cinema
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cinema
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cinema` DEFAULT CHARACTER SET utf8;
USE `cinema`;

-- -----------------------------------------------------
-- Table `cinema`.`film`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinema`.`film`
(
  `ID`             INT(11)     NOT NULL AUTO_INCREMENT,
  `name`           VARCHAR(40) NULL DEFAULT NULL,
  `duration`       INT(11)     NULL DEFAULT NULL,
  `description`    TEXT        NULL DEFAULT NULL,
  `genre`          VARCHAR(25) NULL DEFAULT NULL,
  `country`        VARCHAR(30) NULL DEFAULT NULL,
  `director`       VARCHAR(30) NULL DEFAULT NULL,
  `is3D`           TINYINT(1)  NULL DEFAULT NULL,
  `ageLimit`       INT(11)     NULL DEFAULT NULL,
  `yearProduction` INT(11)     NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 18
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cinema`.`hall`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinema`.`hall`
(
  `ID`           INT(11)     NOT NULL AUTO_INCREMENT,
  `type`         VARCHAR(20) NULL DEFAULT NULL,
  `name`         VARCHAR(35) NULL DEFAULT NULL,
  `floor`        INT(11)     NULL DEFAULT NULL,
  `description`  TEXT        NULL DEFAULT NULL,
  `managerPhone` VARCHAR(17) NULL DEFAULT NULL,
  `capacity`     INT(11)     NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cinema`.`seance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinema`.`seance`
(
  `ID`          INT(11) NOT NULL AUTO_INCREMENT,
  `hallID`      INT(11) NULL DEFAULT NULL,
  `filmID`      INT(11) NULL DEFAULT NULL,
  `seanceTime`  TIME    NOT NULL,
  `seanceDate`  DATE    NOT NULL,
  `ticketCost`  DOUBLE  NOT NULL,
  `ticketsLeft` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  INDEX `seance_hall_ID_fk` (`hallID` ASC) VISIBLE,
  INDEX `seance_film_ID_fk_idx` (`filmID` ASC) VISIBLE,
  CONSTRAINT `seance_film_ID_fk`
    FOREIGN KEY (`filmID`)
      REFERENCES `cinema`.`film` (`ID`)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  CONSTRAINT `seance_hall_ID_fk`
    FOREIGN KEY (`hallID`)
      REFERENCES `cinema`.`hall` (`ID`)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 51
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cinema`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinema`.`user`
(
  `ID`       INT(11)                         NOT NULL AUTO_INCREMENT,
  `login`    VARCHAR(15)                     NOT NULL,
  `password` VARCHAR(15)                     NOT NULL,
  `role`     ENUM ('User', 'Moder', 'Admin') NOT NULL,
  `surname`  VARCHAR(15)                     NULL DEFAULT NULL,
  `name`     VARCHAR(15)                     NULL DEFAULT NULL,
  `email`    VARCHAR(30)                     NULL DEFAULT NULL,
  `birthday` DATE                            NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cinema`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinema`.`ticket`
(
  `ID`         INT(11)       NOT NULL AUTO_INCREMENT,
  `seanceID`   INT(11)       NOT NULL,
  `userID`     INT(11)       NOT NULL,
  `amount`     INT(11)       NOT NULL,
  `cost`       DOUBLE        NOT NULL,
  `seatNumber` VARCHAR(2000) NOT NULL,
  `valid`      TINYINT(1)    NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `ticket_seance_ID_fk` (`seanceID` ASC) VISIBLE,
  INDEX `ticket_user_ID_fk` (`userID` ASC) VISIBLE,
  CONSTRAINT `ticket_seance_ID_fk`
    FOREIGN KEY (`seanceID`)
      REFERENCES `cinema`.`seance` (`ID`),
  CONSTRAINT `ticket_user_ID_fk`
    FOREIGN KEY (`userID`)
      REFERENCES `cinema`.`user` (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 72
  DEFAULT CHARACTER SET = utf8;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
