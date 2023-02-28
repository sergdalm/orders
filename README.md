API for saving orders and sending email with order confirmations.

DLL script:
CREATE TABLE IF NOT EXISTS orders
(
    number         VARCHAR(32)  NOT NULL,
    sum            NUMERIC      NOT NULL,
    creation_date  DATE         NOT NULL,
    customer_email VARCHAR(128) NOT NULL,
    email_sent     BOOLEAN      NOT NULL
);