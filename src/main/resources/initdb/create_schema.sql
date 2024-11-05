CREATE DATABASE IF NOT EXISTS `stock`;
USE stock;

CREATE USER IF NOT EXISTS `min9805`@`localhost` IDENTIFIED BY 'backend';
CREATE USER `min9805`@`%` IDENTIFIED BY 'backend';

GRANT all privileges ON `stock`.* TO `min9805`@`localhost`;
GRANT all privileges ON `stock`.* TO `min9805`@`%`;