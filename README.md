API for saving orders and sending notifications to customers by email.

DLL script:
CREATE TABLE IF NOT EXISTS orders
(
    number         VARCHAR(32)  PRIMARY KEY ,
    sum            NUMERIC      NOT NULL,
    creation_date  DATE         NOT NULL,
    customer_email VARCHAR(128) NOT NULL,
    email_sent     BOOLEAN      NOT NULL
);