CREATE TABLE user
(
    id    BIGINT       NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    name  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

INSERT INTO user (email, name) VALUE ('stefan.jucker@gmail.com', 'Stefan Jucker');
