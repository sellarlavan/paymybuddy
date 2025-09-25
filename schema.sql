CREATE DATABASE IF NOT EXISTS paymybuddydb
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE paymybuddydb;

-- Supprime les tables si elles existent déjà
DROP TABLE IF EXISTS user_connections;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;

-- Table users
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_username (username),
  UNIQUE KEY uq_users_email (email)
) ENGINE=InnoDB;

-- Table transactions
CREATE TABLE transactions (
  id BIGINT NOT NULL AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  description VARCHAR(255),
  amount DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_transactions_sender_idx (sender_id),
  KEY fk_transactions_receiver_idx (receiver_id),
  CONSTRAINT fk_transactions_sender FOREIGN KEY (sender_id)
    REFERENCES users (id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_transactions_receiver FOREIGN KEY (receiver_id)
    REFERENCES users (id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- Table user_connections
CREATE TABLE user_connections (
  user_id    BIGINT NOT NULL,
  contact_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, contact_id),
  KEY fk_userconnections_user_idx (user_id),
  KEY fk_userconnections_contact_idx (contact_id),
  CONSTRAINT fk_userconnections_user FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT fk_userconnections_contact FOREIGN KEY (contact_id)
    REFERENCES users (id)
    ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB;
