CREATE DATABASE IF NOT EXISTS ATMDatabase;
USE ATMDatabase;

CREATE TABLE IF NOT EXISTS users(
id INTEGER PRIMARY KEY AUTO_INCREMENT,
lastname VARCHAR(50) NOT NULL,
firstname VARCHAR(50) NOT NULL,
email VARCHAR(30) NOT NULL UNIQUE,
cardnumber VARCHAR(8) NOT NULL,
pin VARCHAR(4) NOT NULL,
balance BIGINT default 0 NOT NULL,
is_authorized boolean default false NOT NULL,
is_verified boolean default false NOT NULL
);

