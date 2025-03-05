-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema salarytrackerDev
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema salarytrackerDev
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `salarytrackerDevDev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `salarytrackerDev` ;

-- -----------------------------------------------------
-- Table `salarytrackerDev`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salarytrackerDev`.`users` (
  `created_at` DATETIME(6) NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `username` VARCHAR(255) NULL DEFAULT NULL,
  `role` ENUM('ADMIN', 'USER') NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_6dotkott2kjsp8vw4d0m25fb7` (`email` ASC) VISIBLE,
  UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6` (`username` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `salarytrackerDev`.`salaries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salarytrackerDev`.`salaries` (
  `amount` INT NULL DEFAULT NULL,
  `year` INT NOT NULL,
  `created_at` DATETIME(6) NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `month` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKtn3jsh6use2vvoks6qjo4qmb1` (`user_id` ASC, `month` ASC, `year` ASC) VISIBLE,
  CONSTRAINT `FKos57j8u42tdn3d132be45fsh9`
    FOREIGN KEY (`user_id`)
    REFERENCES `salarytrackerDev`.`users` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `salarytrackerDev`.`expenses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salarytrackerDev`.`expenses` (
  `amount` INT NULL DEFAULT NULL,
  `status` BIT(1) NULL DEFAULT NULL,
  `created_at` DATETIME(6) NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `salary_id` BIGINT NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `bank` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `budgetRuleAllocation` ENUM('NEEDS', 'SAVINGS', 'WANTS') NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKnbrsbdy8yc986yd1ul3w9hpc` (`salary_id` ASC) VISIBLE,
  CONSTRAINT `FKnbrsbdy8yc986yd1ul3w9hpc`
    FOREIGN KEY (`salary_id`)
    REFERENCES `salarytrackerDev`.`salaries` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 48
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `salarytrackerDev`.`summary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salarytrackerDev`.`summary` (
  `remaining_salary` INT NULL DEFAULT NULL,
  `total_expense` INT NULL DEFAULT NULL,
  `created_at` DATETIME(6) NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `salary_id` BIGINT NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK14cocms0atw936mt80yg6e7i4` (`salary_id` ASC) VISIBLE,
  CONSTRAINT `FK14cocms0atw936mt80yg6e7i4`
    FOREIGN KEY (`salary_id`)
    REFERENCES `salarytrackerDev`.`salaries` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
