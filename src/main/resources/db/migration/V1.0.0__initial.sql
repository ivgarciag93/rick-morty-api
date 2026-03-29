CREATE TABLE character
(
    id          INTEGER         NOT NULL,
    name        VARCHAR(255)    NOT NULL,
    status      VARCHAR(255)    NOT NULL,
    specie      VARCHAR(255)    NOT NULL,
    gender      VARCHAR(255)    NOT NULL,
    image       VARCHAR(255),
    PRIMARY KEY(id)
);