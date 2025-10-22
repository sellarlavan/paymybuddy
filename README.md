# paymybuddy

PayMyBuddy est une application permettant à des particuliers de s'envoyer de l'argent facilement et en toute sécurité.

# Installation

## Prérequis

- Java 21+
- MySQL 8+
- Maven

## Configuration

**Base de données:**

Lancer le script SQL se trouvant dans le répertoire resources.  
mysql -u root -p < chemin/schema.sql

**Variables d'environnement :**

setx DB_URL "jdbc:mysql://localhost:3306/db"  
setx DB_USER "user"  
setx DB_PASSWORD "password"

**Lancement de l'application et test**

L'application se lance avec le fichier PayMyBuddyApplication. Deux utilisateurs de test sont créés, ainsi qu'une relation permettant des envois d'argent:  
Email: user1@gmail.com  
Mot de passe: password  

Email: user2@gmail.com  
Mot de passe: password  

# Modèle Physique de Données

## Tables

### Table `users`
- id : BIGINT, clé primaire, auto-incrémentée, NOT NULL
- username : VARCHAR(50), UNIQUE, NOT NULL
- email : VARCHAR(255), UNIQUE, NOT NULL
- password : VARCHAR(255), NOT NULL
- balance : DECIMAL(38,2) NOT NULL

### Table `transactions`
- id : BIGINT, clé primaire, auto-incrémentée, NOT NULL
- sender_id : BIGINT, clé étrangère vers users(id), NOT NULL
- receiver_id : BIGINT, clé étrangère vers users(id), NOT NULL
- description : VARCHAR(255), optionnel
- amount : DECIMAL(12,2), NOT NULL

### Table `user_connections`
- user_id : BIGINT, clé étrangère vers users(id), NOT NULL
- contact_id : BIGINT, clé étrangère vers users(id), NOT NULL
- clé primaire composée : (user_id, contact_id)

## Diagramme

![Modèle Physique de Données](docs/mpd.png)
